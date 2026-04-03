package com.drmj.work_tracker.exception;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.utils.ApiResponseConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleNotFoundException(NotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(
                ApiResponseConstants.NOT_FOUND_CODE,
                ApiResponseConstants.NOT_FOUND_MESSAGE,
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}