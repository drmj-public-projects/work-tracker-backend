package com.drmj.work_tracker.dto.response.invitationCode;

import com.drmj.work_tracker.entity.InvitationCode;
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
public class InvitationCodeResponse {
    private UUID id;
    private UUID organizationId;
    private String code;
    private UserOrganizationRole role;
    private OffsetDateTime expiresAt;
    private Integer maxUses;
    private Integer currentUses;
    private Boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static InvitationCodeResponse fromEntity(InvitationCode invitationCode) {
        return InvitationCodeResponse.builder()
                .id(invitationCode.getId())
                .organizationId(invitationCode.getOrganizationId())
                .code(invitationCode.getCode())
                .role(invitationCode.getRole())
                .expiresAt(invitationCode.getExpiresAt())
                .maxUses(invitationCode.getMaxUses())
                .currentUses(invitationCode.getCurrentUses())
                .isActive(invitationCode.getIsActive())
                .createdAt(Utils.toOffsetDateTime(invitationCode.getCreatedAt()))
                .updatedAt(Utils.toOffsetDateTime(invitationCode.getUpdatedAt()))
                .build();
    }
}