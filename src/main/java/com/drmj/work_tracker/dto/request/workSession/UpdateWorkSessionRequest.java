package com.drmj.work_tracker.dto.request.workSession;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
public class UpdateWorkSessionRequest {
    @NotNull
    private OffsetDateTime startTime;
    @NotNull
    private OffsetDateTime endTime;
    private Integer breakMinutes;
}
