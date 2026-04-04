package com.drmj.work_tracker.utils;

import org.springframework.http.HttpStatus;

public final class ApiResponseConstants {
    private ApiResponseConstants() {}

    public static final int SUCCESS_CODE = HttpStatus.OK.value();
    public static final int CREATED_CODE = HttpStatus.CREATED.value();;
    public static final int NOT_FOUND_CODE = HttpStatus.NOT_FOUND.value();
    public static final int UNAUTHORIZED_CODE = HttpStatus.UNAUTHORIZED.value();
    public static final int ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
    public static final int FAIL_CODE = HttpStatus.BAD_REQUEST.value();

    public static final String SUCCESS_MESSAGE = "Operation completed successfully";
}
