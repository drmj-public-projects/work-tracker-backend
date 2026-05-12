package com.drmj.work_tracker.usecase.user;

import com.drmj.work_tracker.dto.request.user.UpdateUserRequest;
import com.drmj.work_tracker.dto.response.user.UserResponse;
import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateCurrentUserUseCase {
    private final UserService userService;

    @Transactional
    public UserResponse execute(UpdateUserRequest request) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        User updatedUser = userService.update(
                currentUserId,
                request.getName(),
                request.getTimezone()
        );
        return UserResponse.fromEntity(updatedUser);
    }
}