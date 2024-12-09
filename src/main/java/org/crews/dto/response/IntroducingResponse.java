package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.*;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class IntroducingResponse {
    private String image;
    private String introduce;
    private String content;
    private AddressResponse address;
    private String subject;
    private List<InterestingResponse> interests;
    private String agitName;

    public static IntroducingResponse of(Introducing introducing){
        List<InterestingResponse> interestingResponse = introducing.getAgit().getInterestingAndAgits().stream()
                .map(interestingAndAgit -> InterestingResponse.from(interestingAndAgit.getInteresting()))
                .toList();
        AddressResponse agitAddress = AddressResponse.from(introducing.getAgit().getAddress());

        return new IntroducingResponse(
                introducing.getImage(),
                introducing.getIntroduce(),
                introducing.getContent(),
                agitAddress,
                introducing.getAgit().getSubject().getSubjectName(),
                interestingResponse,
                introducing.getAgit().getAgitName()
        );
    }
}