package com.drmj.work_tracker.service;

import com.drmj.work_tracker.dto.response.auth.LoginResponse;
import com.drmj.work_tracker.dto.response.auth.TokenResponse;

import java.util.UUID;

public interface AuthService {
    LoginResponse login(String email, String password);

    TokenResponse createTokenWithContext(UUID userId, UUID organizationId);
}
