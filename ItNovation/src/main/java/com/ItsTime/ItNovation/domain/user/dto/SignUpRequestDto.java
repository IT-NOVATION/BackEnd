package com.ItsTime.ItNovation.domain.user.dto;


import lombok.*;

@ToString
@Getter

public class SignUpRequestDto {

    private final String email;
    private final String password;

    @Builder
    public SignUpRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
