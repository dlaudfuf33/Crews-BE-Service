package org.crews.service;

import lombok.RequiredArgsConstructor;
import org.crews.dto.response.PaymentInfoResponse;
import org.crews.model.Membership;
import org.crews.model.constants.AgitRole;
import org.crews.model.constants.CardName;
import org.crews.repository.MemberShipRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
    private final MemberShipRepository memberShipRepository;

    public List<PaymentInfoResponse> getPaymentInfo(Long memberId) {
        List<Membership> memberships = memberShipRepository.findByMemberIdAndAgitRoleNot(memberId, AgitRole.TEMP);
        List<PaymentInfoResponse> paymentInfoResponses = new ArrayList<>();

        Iterator<Membership> membershipIterator = memberships.iterator();

        while(membershipIterator.hasNext()) {
            Membership membership =  membershipIterator.next();
            paymentInfoResponses.add(PaymentInfoResponse.builder().name(membership.getAgit().getAgitName()).agitRole(membership.getAgitRole()).agitId(membership.getAgit().getId()).cardName(CardName.WOORI_CARD.getType()).build());
        }

        return paymentInfoResponses;
    }
}
