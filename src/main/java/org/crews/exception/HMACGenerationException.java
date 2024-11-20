package org.crews.exception;

import lombok.Getter;

@Getter
public class HMACGenerationException extends RuntimeException {
    public HMACGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public HMACGenerationException(String message) {
        super(message);
    }
}
