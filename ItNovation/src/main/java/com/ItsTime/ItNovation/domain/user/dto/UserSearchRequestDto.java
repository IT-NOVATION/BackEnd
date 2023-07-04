package com.ItsTime.ItNovation.domain.user.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchRequestDto {

    private String search;


    @Builder
    public UserSearchRequestDto(String search){
        this.search=search;
    }


}
