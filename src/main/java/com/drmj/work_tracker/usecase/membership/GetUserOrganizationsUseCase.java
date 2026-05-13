package com.drmj.work_tracker.usecase.membership;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.membership.UserOrganizationResponse;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.UserOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetUserOrganizationsUseCase {
    private final UserOrganizationService userOrganizationService;

    public ApiResponse<List<UserOrganizationResponse>> execute() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        
        List<UserOrganization> userOrganizations = userOrganizationService.findByUserId(currentUserId);
        
        List<UserOrganizationResponse> response = userOrganizations.stream()
                .map(UserOrganizationResponse::fromEntity)
                .collect(Collectors.toList());
        
        return new ApiResponse<>(response);
    }
}
