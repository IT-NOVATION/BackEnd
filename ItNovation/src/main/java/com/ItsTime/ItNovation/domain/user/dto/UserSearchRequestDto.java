package com.ItsTime.ItNovation.domain.user.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSearchRequestDto {

    private String searchNickName;


    public UserSearchRequestDto(String searchNickName){
        this.searchNickName =searchNickName;
    }


}
