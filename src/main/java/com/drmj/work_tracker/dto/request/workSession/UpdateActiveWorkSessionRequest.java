package com.drmj.work_tracker.dto.request.workSession;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateActiveWorkSessionRequest {
    @NotNull
    private UUID workSessionId;
    private String notes;
    private Integer breakMinutes;
}
