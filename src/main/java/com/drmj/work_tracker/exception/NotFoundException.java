package com.drmj.work_tracker.exception;

import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final String errorCode;

    public NotFoundException(String message) {
        super(message);
        this.errorCode = null;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = null;
    }

    public NotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorCode = errorMessage.getErrorCode();
    }

    public NotFoundException(ErrorMessage errorMessage, Throwable cause) {
        super(errorMessage.getMessage(), cause);
        this.errorCode = errorMessage.getErrorCode();
    }
}