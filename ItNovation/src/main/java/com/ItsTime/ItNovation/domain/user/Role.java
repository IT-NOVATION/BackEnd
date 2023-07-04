package com.ItsTime.ItNovation.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
//TODO: enum 공부하기, ROLE_의 의미 및 권한 다른것으로 가져가도 되는지 알아보기
public enum Role {
    USER("ROLE_USER"), GUEST("ROLE_GUEST");
    private final String key;
}
