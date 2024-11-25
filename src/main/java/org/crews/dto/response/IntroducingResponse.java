package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Interesting;
import org.crews.model.InterestingAndAgit;
import org.crews.model.Introducing;
import org.crews.model.Subject;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@AllArgsConstructor
public class IntroducingResponse {
    private String memberRole;
    private String image;
    private String introduce;
    private String content;
    private String subject;
    private List<InterestingResponse> interests;

    public static IntroducingResponse of(String memberRole, Introducing introducing){
        List<InterestingResponse> interestingResponse = introducing.getAgit().getInterestingAndAgits().stream()
                .map(interestingAndAgit -> InterestingResponse.from(interestingAndAgit.getInteresting()))
                .toList();
        return new IntroducingResponse(
                memberRole,
                introducing.getImage(),
                introducing.getIntroduce(),
                introducing.getContent(),
                introducing.getAgit().getSubject().getSubjectName(),
                interestingResponse
        );
    }
}
