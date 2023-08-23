package com.ItsTime.ItNovation.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

     //400 Bad Request
     BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
     INCORRECT_EMAIL_CODE(HttpStatus.BAD_REQUEST, "잘못된 인증코드입니다."),

    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "리소스 접근 권한이 없습니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."),
    INVALID_ACCESS_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "액세스 토큰의 값이 올바르지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰의 형식이 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "리프레시 토큰의 값이 올바르지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요."),
    NOT_MATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "일치하지 않는 리프레시 토큰입니다."),

    //403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다."),

    //404 Not Found
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND,"엔티티를 찾을 수 없습니다"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"유저가 존재하지 않습니다"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,"리뷰가 존재하지 않습니다"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"댓글이 존재하지 않습니다"),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP method 요청입니다."),

    //409 Conflict
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),
    NICKNAME_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    SAME_NICKNAME_CONFLICT(HttpStatus.CONFLICT, "기존에 존재하는 닉네임과 일치합니다."),
    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    SEND_MAIL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "메일 전송 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
