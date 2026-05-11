package com.drmj.work_tracker.dto.request.invitationCode;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class GenerateInvitationCodeRequest {
    @NotNull
    private UUID organizationId;

    private OffsetDateTime expiresAt;

    private Integer maxUses = 1;
}
