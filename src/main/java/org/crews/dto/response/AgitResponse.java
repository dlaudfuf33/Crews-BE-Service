package org.crews.dto.response;

import lombok.*;
import org.crews.model.Agit;
import org.crews.model.Interesting;
import org.crews.model.InterestingAndAgit;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgitResponse {
    private Long id;
    private String name;
    private Integer currentNum;
    private Integer maxNum;
    private String introduction;
    private String image;
    private String subject;
    private List<String> interests;

    public static AgitResponse from(Agit agit){
        String introduction = agit.getIntroduction();
        String image = agit.getIntroducing() != null ? agit.getIntroducing().getImage() : "";

        String subject = agit.getSubject() != null ? agit.getSubject().getSubjectName() : "";

        List<String> interests = agit.getInterestingAndAgits().stream()
                .map(InterestingAndAgit::getInteresting)
                .map(Interesting::getName).toList();

        return new AgitResponse(
                agit.getId(),
                agit.getAgitName(),
                agit.getCurrentPerson(),
                agit.getMaxPerson(),
                introduction,
                image,
                subject,
                interests
        );
    }
}
