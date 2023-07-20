package com.ItsTime.ItNovation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GeneralErrorCode {
    UNKNOWN_USER(2000, "해당하는 유저가 존재하지 않습니다."),
    NO_REVIEW(2001, "작성한 리뷰가 없습니다"),
    DUPLICATED_NICKNAME(2002, "이미 존재하는 닉네임 입니다"),

    CONFLICT_NICKNAME(2003, "기존에 존재하는 닉네임과 유사합니다");



    private int code;
    private String message;

    }
