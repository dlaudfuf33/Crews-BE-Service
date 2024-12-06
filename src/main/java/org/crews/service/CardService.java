package org.crews.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crews.dto.core.CardIssuedResponse;
import org.crews.dto.core.CommonRequest;
import org.crews.dto.core.CoreCardRemoveRequest;
import org.crews.dto.core.MessageResponse;
import org.crews.dto.request.CardRemoveRequest;
import org.crews.dto.response.CardIssuanceResponse;
import org.crews.exception.CustomException;
import org.crews.exception.ErrorCode;
import org.crews.model.*;
import org.crews.model.constants.AgitRole;
import org.crews.repository.*;
import org.crews.utils.AESUtil;
import org.crews.utils.MaskedNumber;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {

    private final AgitRepository agitRepository;
    private final MemberShipRepository memberShipRepository;
    private final MemberRepository memberRepository;
    private final CardRepository cardRepository;
    private final CoreService coreService;

    @Transactional
    public CardIssuedResponse cardIssued(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_MATCHED)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        if(agit.getAgitAndAccount() == null)
            throw new CustomException(ErrorCode.AGIT_ACCOUNT_NOT_FOUND);

        Account account = agit.getAgitAndAccount().getAccount();
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        if(membership.getAgitRole().equals(AgitRole.MEMBER) || membership.getAgitRole().equals(AgitRole.ADVANCED) || membership.getAgitRole().equals(AgitRole.TEMP)){
            throw new CustomException(ErrorCode.AUTHORIZED_CAPTAIN_ONLY);
        }
        List<Card> cardList = cardRepository.findByAccountAndMemberAndIsDeletedFalse(account, member);
        if(!cardList.isEmpty())
            throw new CustomException(ErrorCode.CARD_ALREADY_EXISTS);
        String fintechUseNum = account.getFintecNumber();
        CommonRequest commonRequest = CommonRequest.builder()
                .ci(member.getCi())
                .fintechUseNum(fintechUseNum)
                .build();

        CardIssuedResponse response = coreService.cardIssued(commonRequest);
        Card card = Card.builder().account(account).member(member)
                .maskedCardNumber(MaskedNumber.cardMasking(response.getCardNumber()))
                .cardNumber(AESUtil.encrypt(response.getCardNumber())).registeredAt(response.getCreateAt())
                .isDeleted(false).cardImage("").cardName("우리카드 카드의 정석 EVERY POINT").build();

        cardRepository.save(card);
        return response;
    }

    @Transactional
    public MessageResponse cardRemove(Long agitId, Long memberId, CardRemoveRequest cardReissuedRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        if(agit.getAgitAndAccount() == null)
            throw new CustomException(ErrorCode.AGIT_ACCOUNT_NOT_FOUND);
        Account account = agit.getAgitAndAccount().getAccount();
        String fintechUseNum = account.getFintecNumber();
        Membership membership = memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        if(membership.getAgitRole().equals(AgitRole.MEMBER) || membership.getAgitRole().equals(AgitRole.ADVANCED) || membership.getAgitRole().equals(AgitRole.TEMP)){
            throw new CustomException(ErrorCode.AUTHORIZED_CAPTAIN_ONLY);
        }
        CoreCardRemoveRequest coreCardRemoveRequest = CoreCardRemoveRequest.builder().ci(member.getCi())
                .fintechUseNum(fintechUseNum)
                .cardNumber(cardReissuedRequest.getCardNumber()).build();
        System.out.println("coreCardRemoveRequest.getCardNumber() = " + coreCardRemoveRequest.getCardNumber());
        MessageResponse response = coreService.cardRemove(coreCardRemoveRequest);
        Card card = cardRepository.findByCardNumberAndIsDeletedFalse(AESUtil.encrypt(coreCardRemoveRequest.getCardNumber())).orElse(null);
        card.setDeleted(true);
        return response;

    }




    public CardIssuanceResponse cardIssuance(Long agitId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBER_NOT_MATCHED)
        );
        Agit agit = agitRepository.findById(agitId).orElseThrow(
                () -> new CustomException(ErrorCode.AGIT_NOT_FOUND)
        );
        if(agit.getAgitAndAccount() == null)
            throw new CustomException(ErrorCode.AGIT_ACCOUNT_NOT_FOUND);

        Account account = agit.getAgitAndAccount().getAccount();
        memberShipRepository.findByMemberAndAgit(member, agit).orElseThrow(
                () -> new CustomException(ErrorCode.MEMBERSHIP_NOT_FOUND)
        );
        List<Card> cardList = cardRepository.findByAccountAndMemberAndIsDeletedFalse(account, member);
        if(!cardList.isEmpty())
            return CardIssuanceResponse.builder().isCardExist(true).cardNumber(AESUtil.decrypt(cardList.get(0).getCardNumber())).build();
        return CardIssuanceResponse.builder().isCardExist(false).cardNumber(null).build();
    }
}
