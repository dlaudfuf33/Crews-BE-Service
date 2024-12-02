package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.CardIssuedResponse;
import org.crews.dto.core.CommonRequest;
import org.crews.dto.core.CoreCardRemoveRequest;
import org.crews.dto.core.MessageResponse;
import org.crews.dto.request.AccountLinkRequest;
import org.crews.dto.request.CardRemoveRequest;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AgitRole;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final AgitRepository agitRepository;
    private final AccountRepository accountRepository;
    private final MemberShipRepository memberShipRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final CoreService coreService;

    @Transactional
    public CardIssuedResponse cardIssued(Long agitId, Long accountId, AccountLinkRequest accountLinkRequest) {
        Member member = memberRepository.findById(accountLinkRequest.getMemberId()).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_MATCHED)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        Account account = accountRepository.findByIdAndFintecNumber(accountId, accountLinkRequest.getFintechUseNum()).orElseThrow(
                () -> new CustomException(ErrorCode.ACCOUNT_NOT_MATCHED_FINNUM)
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        if(membership.getAgitRole().equals(AgitRole.MEMBER)){
            throw new CustomException(ErrorCode.AUTHORIZED_CAPTAIN_ONLY);
        }
        List<Card> cardList = cardRepository.findByAccountAndMemberAndIsDeletedFalse(account, member);
        if(!cardList.isEmpty())
            throw new CustomException(ErrorCode.CARD_ALREADY_EXISTS);

        CommonRequest commonRequest = CommonRequest.builder()
                .ci(member.getCi())
                .fintechUseNum(accountLinkRequest.getFintechUseNum())
                .build();

        CardIssuedResponse response = coreService.cardIssued(commonRequest);
        Card card = Card.builder().account(account).member(member)
                .maskedCardNumber(cardMasking(response.getCardNumber()))
                .cardNumber(AESUtil.encrypt(response.getCardName())).registeredAt(response.getCreateAt())
                .isDeleted(false).build();

        cardRepository.save(card);
        return response;
    }

    @Transactional
    public MessageResponse cardRemove(Long agitId, Long accountId, CardRemoveRequest cardReissuedRequest) {
        Member member = memberRepository.findById(cardReissuedRequest.getMemberId()).orElseThrow(
                () -> new IllegalStateException("해당하는 번호의 멤버가 없습니다.")
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new IllegalStateException("해당하는 번호의 아지트가 없습니다.")
        );
        accountRepository.findByIdAndFintecNumber(accountId, cardReissuedRequest.getFintechUseNum()).orElseThrow(
                () -> new IllegalStateException("핀테크 번호에 해당하는 계좌가 없습니다.")
        );
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new IllegalStateException("해당하는 아지트의 멤버가 아닙니다.")
        );
        if(membership.getAgitRole().equals(AgitRole.MEMBER)){
            throw new IllegalStateException("모임장이나 공동모임장만 카드를 해지 할 수 있습니다.");
        }
        CoreCardRemoveRequest coreCardRemoveRequest = CoreCardRemoveRequest.builder().ci(member.getCi())
                .fintechUseNum(cardReissuedRequest.getFintechUseNum())
                .cardNumber(cardReissuedRequest.getCardNumber()).build();
        MessageResponse response = coreService.cardRemove(coreCardRemoveRequest);
        Card card = cardRepository.findByCardNumberAndIsDeletedFalse(coreCardRemoveRequest.getCardNumber()).orElse(null);
        if(card != null){
            cardRepository.delete(card);
        }
        return response;

    }


    // 카드번호 가운데 8자리 마스킹
    private String cardMasking(String cardNo) {
        // 카드번호 16자리 또는 15자리 '-'포함/미포함 상관없음
        String regex = "(\\d{4})-?(\\d{4})-?(\\d{4})-?(\\d{3,4})$";

        Matcher matcher = Pattern.compile(regex).matcher(cardNo);
        if(matcher.find()) {
            String target = matcher.group(2) + matcher.group(3);
            int length = target.length();
            char[] c = new char[length];
            Arrays.fill(c, '*');

            return cardNo.replace(target, String.valueOf(c));
        }
        return cardNo;
    }
}
