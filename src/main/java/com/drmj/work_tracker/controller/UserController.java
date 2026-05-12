package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.user.UpdateUserRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.user.UserResponse;
import com.drmj.work_tracker.usecase.user.GetCurrentUserUseCase;
import com.drmj.work_tracker.usecase.user.UpdateCurrentUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    private final UpdateCurrentUserUseCase updateCurrentUserUseCase;

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {
        return new ApiResponse<>(getCurrentUserUseCase.execute());
    }
    @PutMapping("/me")
    public ApiResponse<UserResponse> updateCurrentUser(
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return new ApiResponse<>(updateCurrentUserUseCase.execute(request));
    }
}
