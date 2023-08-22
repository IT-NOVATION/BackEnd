package com.ItsTime.ItNovation.common.handler;

import com.ItsTime.ItNovation.common.exception.*;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity handleForbiddenException(BadRequestException e){
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleJwtException(UnauthorizedException e){
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBaseResponse);
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity handleForbiddenException(ForbiddenException e){
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorBaseResponse);
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleForbiddenException(NotFoundException e){
        final ErrorBaseResponse errorBaseResponse = ErrorBaseResponse.of(e.getErrorCode());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBaseResponse);
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
