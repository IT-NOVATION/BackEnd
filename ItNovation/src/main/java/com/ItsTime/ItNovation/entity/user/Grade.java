package com.ItsTime.ItNovation.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Grade {
    VIP("vip"), COMMON("common");

    private final String key;
}
