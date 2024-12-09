package org.crews.model.constants;

import lombok.Getter;

@Getter
public enum PaymentTargetAccount {
    TARGET_ACCOUNT("1002002742728");
    private final String target;

    PaymentTargetAccount(String target) {
        this.target = target;
    }
}
