package com.drmj.work_tracker.dto.response.organization;

import com.drmj.work_tracker.entity.Organization;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class OrganizationResponse {
    private UUID id;
    private String name;

    public static OrganizationResponse buildFromOrganization(Organization organization) {
        return OrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .build();
    }
}
