package com.ItsTime.ItNovation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode {
    UNKNOWN_USER(2000, "해당하는 유저가 존재하지 않습니다.");


    private int code;
    private String message;

}
