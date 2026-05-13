package com.drmj.work_tracker.dto.request.organization;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateOrganizationSettingsRequest {
    @NotNull
    private UUID organizationId;

    @NotNull
    private Boolean requireLocation;

    @NotNull
    private Boolean allowManualEntries;

    @NotNull
    private Boolean allowEditAfterSubmit;
}
