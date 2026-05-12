package com.drmj.work_tracker.dto.response.membership;

import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.utils.Utils;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembershipResponse {
    private UUID id;
    private UUID userId;
    private UUID organizationId;
    private String organizationName;
    private UserOrganizationRole role;
    private OffsetDateTime joinedAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static MembershipResponse fromEntity(UserOrganization userOrganization) {
        return MembershipResponse.builder()
                .id(userOrganization.getId())
                .userId(userOrganization.getUserId())
                .organizationId(userOrganization.getOrganizationId())
                .organizationName(userOrganization.getOrganization().getName())
                .role(userOrganization.getRole())
                .joinedAt(Utils.toOffsetDateTime(userOrganization.getCreatedAt()))
                .createdAt(Utils.toOffsetDateTime(userOrganization.getCreatedAt()))
                .updatedAt(Utils.toOffsetDateTime(userOrganization.getUpdatedAt()))
                .build();
    }
}
