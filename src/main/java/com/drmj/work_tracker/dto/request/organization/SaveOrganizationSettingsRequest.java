package com.drmj.work_tracker.dto.request.organization;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveOrganizationSettingsRequest {
    @NotNull
    private Boolean requireLocation;

    @NotNull
    private Boolean allowManualEntries;

    @NotNull
    private Boolean allowEditAfterSubmit;

    private String timeZone;
}
