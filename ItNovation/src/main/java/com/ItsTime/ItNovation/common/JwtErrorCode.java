package com.ItsTime.ItNovation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum JwtErrorCode {

    UNKNOWN_TOKEN(1003, "토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(1004, "유효하지 않은 JWT 서명입니다."),
    INVALID_TOKEN(1005,"잘못된 JWT 토큰입니다"),
    EXPIRED_TOKEN(1006, "만료된 토큰입니다."),
    ACCESS_DENIED(1007, "권한이 없습니다."),
    UNKNOWN_REFRESHTOKEN(1008,"리프레시 토큰이 존재하지 않습니다"),
    NO_MEMBER(1009,"일치하는 회원이 없습니다");

    private int code;
    private String message;


}