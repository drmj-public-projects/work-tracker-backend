package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.auth.LoginRequest;
import com.drmj.work_tracker.dto.request.auth.SelectOrganizationRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.auth.LoginResponse;
import com.drmj.work_tracker.dto.response.auth.TokenResponse;
import com.drmj.work_tracker.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return new ApiResponse<>(authService.login(request.getEmail(), request.getPassword()));
    }

    @PostMapping("/select-organization")
    public ApiResponse<TokenResponse> selectOrganization(
            @RequestBody SelectOrganizationRequest request,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        return new ApiResponse<>(authService.createTokenWithContext(userId, request.getOrganizationId()));
    }
}
