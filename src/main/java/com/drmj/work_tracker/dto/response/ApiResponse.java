package com.drmj.work_tracker.dto.response;

import com.drmj.work_tracker.utils.ApiResponseConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public ApiResponse (T data) {
        this.status = ApiResponseConstants.SUCCESS_CODE;
        this.message = ApiResponseConstants.SUCCESS_MESSAGE;
        this.data = data;
    }
}
