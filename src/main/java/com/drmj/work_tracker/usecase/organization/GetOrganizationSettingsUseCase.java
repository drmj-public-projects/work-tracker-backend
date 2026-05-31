package com.drmj.work_tracker.usecase.organization;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationSettingsResponse;
import com.drmj.work_tracker.entity.OrganizationSettings;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.OrganizationSettingsService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetOrganizationSettingsUseCase {
    private final OrganizationSettingsService organizationSettingsService;
    private final UserOrganizationService userOrganizationService;

    public ApiResponse<OrganizationSettingsResponse> execute(UUID organizationId) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        validateUserAccess(currentUserId, organizationId);

        OrganizationSettings settings = organizationSettingsService.findByOrganizationId(organizationId).orElse(null);
        if (settings == null) {
            return new ApiResponse<>(null);
        }
        return new ApiResponse<>(OrganizationSettingsResponse.fromEntity(settings));
    }

    private void validateUserAccess(UUID userId, UUID organizationId) {
        boolean hasAccess = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!hasAccess) {
            throw new BusinessException(ErrorMessage.USER_NOT_IN_ORG.getMessage());
        }
    }
}
