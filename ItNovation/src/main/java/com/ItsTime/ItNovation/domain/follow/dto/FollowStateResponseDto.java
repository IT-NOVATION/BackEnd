package com.ItsTime.ItNovation.domain.follow.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FollowStateResponseDto {


    private Boolean isFollow;

    @Builder
    public FollowStateResponseDto(Boolean isFollow) {
        this.isFollow = isFollow;
    }
}
