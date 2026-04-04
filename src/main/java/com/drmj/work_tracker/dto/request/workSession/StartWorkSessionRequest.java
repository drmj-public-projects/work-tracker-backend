package com.drmj.work_tracker.dto.request.workSession;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class StartWorkSessionRequest {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID organizationId;
    @NotNull
    private UUID placeId;
    private Double latitude;
    private Double longitude;
    private String notes;
}
