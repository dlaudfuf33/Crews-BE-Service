package org.crews.model.constants;

public enum MemberRole {
    LEADER("모임장"), STAFF("공동 모임장"), MEMBER("모임원");
    private final String type;

    MemberRole(String type) {
        this.type = type;
    }
}
