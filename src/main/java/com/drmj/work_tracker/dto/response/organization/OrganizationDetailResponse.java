package com.drmj.work_tracker.dto.response.organization;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@Builder
public class OrganizationDetailResponse {
    private UUID id;
    private String name;
    private OrganizationSettingsResponse settings;

    public static OrganizationDetailResponse buildFromOrganizationAndSettings(
            com.drmj.work_tracker.entity.Organization organization,
            com.drmj.work_tracker.entity.OrganizationSettings settings) {

        return OrganizationDetailResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .settings(settings != null ? OrganizationSettingsResponse.fromEntity(settings) : null)
                .build();
    }
}
