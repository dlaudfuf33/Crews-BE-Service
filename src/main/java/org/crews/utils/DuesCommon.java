package org.crews.utils;

import org.crews.model.Dues;
import org.crews.model.Member;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DuesCommon {

    public static Map<Member, BigDecimal> calculateTotalDueAmountByMembership(List<Dues> duesList) {
        return duesList.stream()
                .filter(d -> d.getMembership() != null) // Membership이 있는 항목만 처리
                .collect(Collectors.groupingBy(
                        d -> d.getMembership().getMember(), // Membership ID로 그룹화
                        Collectors.mapping(
                                Dues::getDueAmount, // dueAmount를 추출
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add) // 합산
                        )
                ));
    }

    public static void setPaidChange(List<Dues> dues, Member filterMember, boolean setPaid) {
        dues.forEach(content -> {
            if (content.getMembership().getMember().equals(filterMember)) {
                content.setPaid(setPaid);
            }
        });
    }
}
