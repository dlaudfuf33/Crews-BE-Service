package org.crews.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);  // cause를 포함
        this.errorCode = errorCode;
    }

    public CustomException(ErrorCode errorCode, String additionalInfo) {
        super(errorCode.getMessage() + ": " + additionalInfo);  // 추가 정보 포함
        this.errorCode = errorCode;
    }

}
