package com.drmj.work_tracker.usecase.organization;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationDetailResponse;
import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.OrganizationSettings;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.OrganizationService;
import com.drmj.work_tracker.service.OrganizationSettingsService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetOrganizationDetailUseCase {
    private final OrganizationService organizationService;
    private final OrganizationSettingsService organizationSettingsService;
    private final UserOrganizationService userOrganizationService;

    public ApiResponse<OrganizationDetailResponse> execute(UUID organizationId) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        validateUserAccess(currentUserId, organizationId);
        Organization organization = organizationService.getById(organizationId);

        OrganizationSettings settings;
        try {
            settings = organizationSettingsService.getByOrganizationId(organizationId);
        } catch (Exception e) {
            settings = null;
        }
        return new ApiResponse<>(OrganizationDetailResponse.buildFromOrganizationAndSettings(organization, settings));
    }

    private void validateUserAccess(UUID userId, UUID organizationId) {
        boolean hasAccess = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!hasAccess) {
            throw new BusinessException(ErrorMessage.USER_NOT_IN_ORG.getMessage());
        }
    }
}
