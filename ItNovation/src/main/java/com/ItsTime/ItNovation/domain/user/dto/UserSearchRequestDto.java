package com.ItsTime.ItNovation.domain.user.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class UserSearchRequestDto {

    private String searchNickName;


    @Builder
    public UserSearchRequestDto(String searchNickName){
        this.searchNickName =searchNickName;
    }


}
