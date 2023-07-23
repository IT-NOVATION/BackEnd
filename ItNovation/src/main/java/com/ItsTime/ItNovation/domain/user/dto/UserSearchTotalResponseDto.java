package com.ItsTime.ItNovation.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserSearchTotalResponseDto {
    private int totalSize;
    private List<UserSearchResponseDto> userSearchResponseDtoList;

    @Builder
    public UserSearchTotalResponseDto(int totalSize, List<UserSearchResponseDto> userSearchResponseDtoList) {
        this.totalSize = totalSize;
        this.userSearchResponseDtoList = userSearchResponseDtoList;
    }
}
