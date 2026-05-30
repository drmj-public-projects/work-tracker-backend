package com.drmj.work_tracker.usecase.organization;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationDetailResponse;
import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.service.OrganizationService;
import com.drmj.work_tracker.service.UserOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOrganizationsByUserUseCase {
    private final UserOrganizationService userOrganizationService;
    private final OrganizationService organizationService;

    public ApiResponse<List<OrganizationDetailResponse>> execute(UUID userId) {
        List<UserOrganization> userOrgs = userOrganizationService.findByUserId(userId);
        List<UUID> organizationIds = userOrgs.stream()
                .map(UserOrganization::getOrganizationId)
                .distinct()
                .collect(Collectors.toList());

        if (organizationIds.isEmpty()) {
            return new ApiResponse<>(Collections.emptyList());
        }

        List<Organization> organizations = organizationService.getAllByIds(organizationIds);
        Map<UUID, Organization> organizationMap = organizations.stream()
                .collect(Collectors.toMap(Organization::getId, o -> o));

        Map<UUID, Long> memberCountMap = userOrganizationService.countMembersByOrganizationIds(organizationIds);

        List<OrganizationDetailResponse> response = organizationIds.stream()
                .map(orgId -> {
                    Organization org = organizationMap.get(orgId);
                    if (org == null) {
                        return null;
                    }
                    return OrganizationDetailResponse.builder()
                            .id(org.getId())
                            .name(org.getName())
                            .memberCount(memberCountMap.getOrDefault(orgId, 0L))
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new ApiResponse<>(response);
    }
}
