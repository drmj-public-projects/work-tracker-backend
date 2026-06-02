package com.drmj.work_tracker.usecase.hourlyRate;

import com.drmj.work_tracker.dto.request.hourlyRate.UpdateHourlyRateRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.hourlyRate.HourlyRateResponse;
import com.drmj.work_tracker.entity.HourlyRate;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.HourlyRateService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateHourlyRateUseCase {
    private final HourlyRateService hourlyRateService;
    private final UserOrganizationService userOrganizationService;

    public ApiResponse<HourlyRateResponse> execute(UUID id, UpdateHourlyRateRequest request) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        UUID organizationId = SecurityUtils.getCurrentOrganizationId();

        validateUserBelongsToOrganization(currentUserId, organizationId);
        validateUserRole(currentUserId, organizationId);

        HourlyRate hourlyRate = hourlyRateService.getById(id);
        if (!hourlyRate.getOrganizationId().equals(organizationId)) {
            throw new BusinessException(ErrorMessage.USER_NOT_ACCESS_TO_ORGANIZATION.getMessage());
        }

        hourlyRate.setRate(request.getRate());
        hourlyRate.setValidFrom(request.getValidFrom());
        hourlyRate.setValidTo(request.getValidTo());

        HourlyRate updated = hourlyRateService.update(hourlyRate);
        return new ApiResponse<>(HourlyRateResponse.fromEntity(updated));
    }

    private void validateUserBelongsToOrganization(UUID userId, UUID organizationId) {
        boolean belongs = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!belongs) {
            throw new BusinessException(ErrorMessage.USER_NOT_ACCESS_TO_ORGANIZATION.getMessage());
        }
    }

    private void validateUserRole(UUID userId, UUID organizationId) {
        UserOrganization userOrg = userOrganizationService.findByUserIdAndOrganizationId(userId, organizationId);
        UserOrganizationRole role = userOrg.getRole();
        if (role != UserOrganizationRole.ADMIN && role != UserOrganizationRole.EMPLOYER) {
            throw new BusinessException(ErrorMessage.USER_NOT_AUTHORIZED_TO_MANAGE_HOURLY_RATES.getMessage());
        }
    }
}
