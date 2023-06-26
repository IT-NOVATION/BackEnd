package com.ItsTime.ItNovation.domain.mail.dto;


import lombok.Data;

@Data
public class RewritePasswordRequestDto {

    private String email;
    private String password;
}
