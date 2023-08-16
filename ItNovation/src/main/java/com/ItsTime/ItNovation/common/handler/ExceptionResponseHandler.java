package com.ItsTime.ItNovation.common.handler;

import com.ItsTime.ItNovation.common.exception.ErrorBaseResponse;
import com.ItsTime.ItNovation.common.exception.ErrorCode;
import com.ItsTime.ItNovation.common.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleJwtException(UnauthorizedException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * 지원하지 않는 HTTP method로 요청 시 발생하는 error를 handling합니다.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorBaseResponse);
    }

}
