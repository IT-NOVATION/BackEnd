package com.ItsTime.ItNovation.domain.mail.dto;


import lombok.Data;
import lombok.Getter;

@Getter
public class RewritePasswordRequestDto {

    private String email;
    private String password;
}
