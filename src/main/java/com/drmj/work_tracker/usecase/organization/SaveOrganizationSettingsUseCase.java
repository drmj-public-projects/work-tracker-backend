package com.drmj.work_tracker.usecase.organization;

import com.drmj.work_tracker.dto.request.organization.SaveOrganizationSettingsRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationSettingsResponse;
import com.drmj.work_tracker.entity.OrganizationSettings;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.OrganizationSettingsService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaveOrganizationSettingsUseCase {
    private final OrganizationSettingsService organizationSettingsService;
    private final UserOrganizationService userOrganizationService;

    private static final List<UserOrganizationRole> ALLOWED_ROLES = Arrays.asList(
            UserOrganizationRole.EMPLOYER,
            UserOrganizationRole.ADMIN
    );

    public ApiResponse<OrganizationSettingsResponse> execute(UUID organizationId, SaveOrganizationSettingsRequest request) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        validateUserAccess(currentUserId, organizationId);
        validateUserRole(currentUserId, organizationId);

        OrganizationSettings settings = organizationSettingsService.getByOrganizationId(organizationId);
        settings.setRequireLocation(request.getRequireLocation());
        settings.setAllowManualEntries(request.getAllowManualEntries());
        settings.setAllowEditAfterSubmit(request.getAllowEditAfterSubmit());

        OrganizationSettings saved = organizationSettingsService.save(settings);

        return new ApiResponse<>(OrganizationSettingsResponse.fromEntity(saved));
    }

    private void validateUserAccess(UUID userId, UUID organizationId) {
        boolean hasAccess = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!hasAccess) {
            throw new BusinessException(ErrorMessage.USER_NOT_IN_ORG.getMessage());
        }
    }

    private void validateUserRole(UUID userId, UUID organizationId) {
        UserOrganization userOrg = userOrganizationService.findByUserIdAndOrganizationId(userId, organizationId);
        if (userOrg == null || !ALLOWED_ROLES.contains(userOrg.getRole())) {
            throw new BusinessException(ErrorMessage.USER_NOT_AUTHORIZED_TO_UPDATE_SETTINGS.getMessage());
        }
    }
}
