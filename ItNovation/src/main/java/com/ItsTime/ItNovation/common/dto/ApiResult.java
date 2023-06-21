package com.ItsTime.ItNovation.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ApiResult<T> {
    private final boolean success;
    private final T response;
    private final String message;

}
