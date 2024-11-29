package org.crews.dto.core;
import lombok.Builder;
import lombok.Getter;
import org.crews.exception.ErrorResponse;

@Getter
@Builder
public class ApiResponse<T> {
    private T data;
    private ErrorResponse error;
    private boolean success;
}
