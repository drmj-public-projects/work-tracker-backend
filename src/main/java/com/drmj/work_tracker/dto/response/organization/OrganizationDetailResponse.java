package com.drmj.work_tracker.dto.response.organization;

import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.OrganizationSettings;
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
    private Long memberCount;

    public static OrganizationDetailResponse buildFromOrganizationAndSettings(
            Organization organization,
            OrganizationSettings settings,
            Long memberCount) {

        return OrganizationDetailResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .settings(settings != null ? OrganizationSettingsResponse.fromEntity(settings) : null)
                .memberCount(memberCount)
                .build();
    }
}
