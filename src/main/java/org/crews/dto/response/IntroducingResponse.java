package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.crews.model.Introducing;

@Getter
@ToString
@AllArgsConstructor
public class IntroducingResponse {
    private String memberRole;
    private String image;
    private String introduce;
    private String content;

    public static IntroducingResponse of(String memberRole, Introducing introducing){
        return new IntroducingResponse(
                memberRole,
                introducing.getImage(),
                introducing.getIntroduce(),
                introducing.getContent()
        );
    }
}
