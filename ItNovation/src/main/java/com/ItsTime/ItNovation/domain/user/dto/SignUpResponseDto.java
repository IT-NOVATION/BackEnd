package com.ItsTime.ItNovation.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@ToString
@Getter

public class SignUpResponseDto {
    private final Long userId;

    @Builder
    public SignUpResponseDto(Long userId) {
        this.userId = userId;
    }
}
