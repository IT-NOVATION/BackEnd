package com.ItsTime.ItNovation.common.exception;

import com.ItsTime.ItNovation.common.exception.BusinessException;
import com.ItsTime.ItNovation.common.exception.ErrorCode;

public class UnauthorizedException extends BusinessException {
    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED);
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
