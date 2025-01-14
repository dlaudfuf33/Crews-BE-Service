package org.crews.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus())
                .body(ErrorResponse.builder()
                        .errorCode(ex.getErrorCode().name())
                        .message(ex.getErrorCode().getMessage())
                        .details(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());

    }
}
