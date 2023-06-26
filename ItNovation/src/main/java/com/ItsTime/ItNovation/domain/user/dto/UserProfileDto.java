package com.ItsTime.ItNovation.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Getter
@ToString
public class UserProfileDto {
    private final String nickname;
    private final String introduction;

    @Builder
    public UserProfileDto(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
