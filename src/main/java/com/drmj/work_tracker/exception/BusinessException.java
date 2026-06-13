package com.drmj.work_tracker.exception;

import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String message) {
        super(message);
        this.errorCode = null;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorCode = errorMessage.getErrorCode();
    }

    public BusinessException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getMessage(), cause);
        this.errorCode = errorMessage.getErrorCode();
    }
}
