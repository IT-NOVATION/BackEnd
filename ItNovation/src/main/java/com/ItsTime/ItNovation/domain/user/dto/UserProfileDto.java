package com.ItsTime.ItNovation.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Getter
@ToString
public class UserProfileDto {
    private final String nickname;
    private final String email;
    private final String introduction;

    @Builder
    public UserProfileDto(String email,String nickname, String introduction) {
        this.email=email;
        this.nickname = nickname;
        this.introduction = introduction;
    }
}
