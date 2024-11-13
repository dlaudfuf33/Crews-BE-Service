package org.crews.model.constants;

public enum TranType {
    DEPOSIT("입금"),
    WITHDRAW("출금");
    private final String type;

    TranType(String type) {
        this.type = type;
    }
}
