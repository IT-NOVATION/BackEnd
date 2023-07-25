package com.ItsTime.ItNovation.domain.mail.dto;


import lombok.Data;
import lombok.Getter;

@Getter
public class CodeCheckRequestDto {

    private String email;
    private String code;

}
