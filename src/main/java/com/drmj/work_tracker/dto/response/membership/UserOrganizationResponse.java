package com.drmj.work_tracker.dto.response.membership;

import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class UserOrganizationResponse {
    private UUID id;
    private UUID organizationId;
    private String organizationName;
    private UserOrganizationRole role;

    public static UserOrganizationResponse fromEntity(UserOrganization userOrganization) {
        Organization org = userOrganization.getOrganization();
        return UserOrganizationResponse.builder()
                .id(userOrganization.getId())
                .organizationId(userOrganization.getOrganizationId())
                .organizationName(org != null ? org.getName() : null)
                .role(userOrganization.getRole())
                .build();
    }
}
