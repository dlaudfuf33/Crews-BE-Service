package org.crews.model.constants;

public enum AgitRole {
    LEADER("모임장"), STAFF("공동 모임장"), ADVANCED("공동 모임장 지원자"), MEMBER("모임원"), TEMP("임시 모임원");
    private final String type;

    AgitRole(String type) {
        this.type = type;
    }
}
