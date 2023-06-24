package com.ItsTime.ItNovation.domain.mail.dto;


import lombok.Data;

@Data
public class CodeCheckRequestDto {

    private String email;
    private String code;

}
