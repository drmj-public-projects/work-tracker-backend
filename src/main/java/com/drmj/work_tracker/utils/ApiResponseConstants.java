package com.drmj.work_tracker.utils;

import org.springframework.http.HttpStatus;

public final class ApiResponseConstants {
    private ApiResponseConstants() {}

    public static final int SUCCESS_CODE = HttpStatus.OK.value();
    public static final String SUCCESS_MESSAGE = "Operation completed successfully";

    public static final int CREATED_CODE = HttpStatus.CREATED.value();;
    public static final String CREATED_MESSAGE = "Resource created successfully";

    public static final int NOT_FOUND_CODE = HttpStatus.NOT_FOUND.value();;
    public static final String NOT_FOUND_MESSAGE = "Resource not found";

    public static final int UNAUTHORIZED_CODE = HttpStatus.UNAUTHORIZED.value();;
    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized";

    public static final int ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();;
    public static final String ERROR_MESSAGE = "Internal server error";
}
