package com.drmj.work_tracker.exception;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.utils.ApiResponseConstants;
import com.drmj.work_tracker.utils.ErrorMessage;
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
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
        return ResponseEntity.badRequest()
                .body(new ApiResponse<>(
                        ApiResponseConstants.FAIL_CODE,
                        ex.getMessage(),
                        null
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new ApiResponse<>(
                        ApiResponseConstants.ERROR_CODE,
                        ErrorMessage.INTERNAL_SERVER_ERROR_MESSAGE.getMessage(),
                        null
                ));
    }
}