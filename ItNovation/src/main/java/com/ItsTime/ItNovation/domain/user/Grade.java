package com.ItsTime.ItNovation.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {
    STANDARD("STANDARD"),
    PREIMUM("PREMIUM"),
    VIP("VIP"),
    SPECIAL("SPECIAL");

    private final String key;


    public static Grade getDefault() {
        return STANDARD;
    }

}
