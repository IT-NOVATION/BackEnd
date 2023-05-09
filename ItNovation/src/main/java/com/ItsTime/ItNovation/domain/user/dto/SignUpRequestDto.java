package com.ItsTime.ItNovation.domain.user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
