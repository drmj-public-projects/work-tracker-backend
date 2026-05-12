package com.drmj.work_tracker.usecase.auth;

import com.drmj.work_tracker.dto.request.auth.RegisterRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.auth.RegisterResponse;
import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.UserService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUseCase {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ApiResponse<RegisterResponse> execute(RegisterRequest request) {
        validatePermission();
        validateEmailNotExists(request.getEmail());

        User user = createUser(request);
        User savedUser = userService.save(user);

        RegisterResponse response = new RegisterResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                "User registered successfully"
        );

        return new ApiResponse<>(response);
    }

    private void validatePermission() {
        String currentRole = SecurityUtils.getCurrentUserRole();
        if (!UserOrganizationRole.ADMIN.name().equals(currentRole)) {
            throw new BusinessException(ErrorMessage.USER_NOT_ALLOWED.getMessage());
        }
    }

    private void validateEmailNotExists(String email) {
        if (userService.existsByEmail(email)) {
            throw new BusinessException(ErrorMessage.USER_EMAIL_ALREADY_EXISTS.getMessage());
        }
    }

    private User createUser(RegisterRequest request) {
        return User.builder()
                .name(request.getName())
                .email(request.getEmail().toLowerCase().trim())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .timezone(request.getTimezone())
                .build();
    }
}
