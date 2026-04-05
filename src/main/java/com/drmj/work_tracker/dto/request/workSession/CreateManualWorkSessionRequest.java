package com.drmj.work_tracker.dto.request.workSession;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateManualWorkSessionRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID organizationId;
    @NotNull
    private UUID placeId;
    @NotNull
    private OffsetDateTime startTime;
    @NotNull
    private OffsetDateTime endTime;
    private String notes;
    private Double latitude;
    private Double longitude;
}
