package com.ItsTime.ItNovation.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorBaseResponse {
    private final int status;
    private final String message;

    @Builder
    public ErrorBaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorBaseResponse of(ErrorCode errorCode) {
        return ErrorBaseResponse.builder()
                .status(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}
