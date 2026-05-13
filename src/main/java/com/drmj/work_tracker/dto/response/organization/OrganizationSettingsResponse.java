package com.drmj.work_tracker.dto.response.organization;

import com.drmj.work_tracker.entity.OrganizationSettings;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class OrganizationSettingsResponse {
    private UUID id;
    private UUID organizationId;
    private Boolean requireLocation;
    private Boolean allowManualEntries;
    private Boolean allowEditAfterSubmit;

    public static OrganizationSettingsResponse fromEntity(OrganizationSettings settings) {
        return OrganizationSettingsResponse.builder()
                .id(settings.getId())
                .organizationId(settings.getOrganizationId())
                .requireLocation(settings.getRequireLocation())
                .allowManualEntries(settings.getAllowManualEntries())
                .allowEditAfterSubmit(settings.getAllowEditAfterSubmit())
                .build();
    }
}
