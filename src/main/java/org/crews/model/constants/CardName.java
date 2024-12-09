package org.crews.model.constants;

import lombok.Getter;

@Getter
public enum CardName {
    WOORI_CARD("우리카드 카드의 정석 EVERY POINT");
    private final String type;

    CardName(String type) {
        this.type = type;
    }
}
