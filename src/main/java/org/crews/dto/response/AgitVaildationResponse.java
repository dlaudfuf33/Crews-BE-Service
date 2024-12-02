package org.crews.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.crews.model.Agit;
import org.crews.model.Feed;
import org.crews.model.Member;
import org.crews.model.Membership;

@Getter
public class AgitVaildationResponse {
    private final Agit agit;
    private final Member member;
    private final Membership membership;
    private final Feed feed;

    public AgitVaildationResponse(Agit agit, Member member, Membership membership) {
        this.agit = agit;
        this.member = member;
        this.membership = membership;
        this.feed = null;
    }
    public AgitVaildationResponse(Agit agit, Member member, Membership membership, Feed feed) {
        this.agit = agit;
        this.member = member;
        this.membership = membership;
        this.feed = feed;
    }
}
