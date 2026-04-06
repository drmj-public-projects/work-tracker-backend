package com.drmj.work_tracker.service;

import com.drmj.work_tracker.dto.response.auth.LoginResponse;
import com.drmj.work_tracker.dto.response.auth.TokenResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationResponse;
import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.repository.UserRepository;
import com.drmj.work_tracker.security.JwtProvider;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserOrganizationService userOrganizationService;

    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorMessage.USER_WITH_EMAIL_NOT_FOUND.getMessage()));
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BusinessException(ErrorMessage.INVALID_CREDENTIALS.getMessage());
        }
        String baseToken = jwtProvider.generateBaseToken(user.getId());
        List<UserOrganization> userOrganizationList = userOrganizationService.findByUserId(user.getId());
        List<OrganizationResponse> organizationList = userOrganizationList.stream()
                .map(UserOrganization::getOrganization)
                .map(OrganizationResponse::buildFromOrganization)
                .toList();
        return new LoginResponse(
                baseToken,
                user.getId(),
                user.getName(),
                user.getEmail(),
                organizationList
        );
    }

    @Override
    public TokenResponse createTokenWithContext(UUID userId, UUID organizationId) {
        UserOrganization userOrganization = userOrganizationService
                .findByUserIdAndOrganizationId(userId, organizationId);
        String token = jwtProvider.generateToken(
                userId,
                organizationId,
                userOrganization.getRole().name()
        );
        return new TokenResponse(token, organizationId, userOrganization.getRole().name());
    }
}
