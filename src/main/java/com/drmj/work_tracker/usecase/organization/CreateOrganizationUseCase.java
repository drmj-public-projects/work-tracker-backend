package com.drmj.work_tracker.usecase.organization;

import com.drmj.work_tracker.dto.request.organization.CreateOrganizationRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationResponse;
import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrganizationUseCase {
    private final OrganizationService organizationService;

    public ApiResponse<OrganizationResponse> execute(CreateOrganizationRequest request) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        Organization organization = organizationService.createOrganization(
                currentUserId,
                request.getName(),
                request.getTimeZone()
        );
        return new ApiResponse<>(OrganizationResponse.buildFromOrganization(organization));
    }
}
