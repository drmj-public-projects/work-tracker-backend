package com.drmj.work_tracker.usecase.user;

import com.drmj.work_tracker.dto.response.user.UserResponse;
import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class GetCurrentUserUseCase {
    private final UserService userService;

    public UserResponse execute() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        User user = userService.getById(currentUserId);
        return UserResponse.fromEntity(user);
    }
}