package com.ItsTime.ItNovation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Code {

    UNKNOWN_ERROR(1003, "토큰이 존재하지 않습니다."),
    WRONG_TYPE_TOKEN(1004, "변조된 토큰입니다."),
    EXPIRED_TOKEN(1005, "만료된 토큰입니다."),
    UNCOMPLETE_TOKEN(1006, " 토큰설정이 완료되지 않았습니다"),
    ACCESS_DENIED(1007, "권한이 없습니다.");

    private int code;
    private String message;


}