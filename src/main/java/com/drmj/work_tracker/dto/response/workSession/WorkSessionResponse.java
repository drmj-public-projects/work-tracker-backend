package com.drmj.work_tracker.dto.response.workSession;

import com.drmj.work_tracker.entity.WorkSession;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class WorkSessionResponse {
    private UUID id;
    private UUID userId;
    private UUID organizationId;
    private UUID placeId;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private Integer durationMinutes;
    private String status;

    public static WorkSessionResponse fromEntity(WorkSession entity) {
        return WorkSessionResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId() != null ? entity.getUserId() : null)
                .organizationId(entity.getOrganizationId() != null ? entity.getOrganizationId() : null)
                .placeId(entity.getPlaceId() != null ? entity.getPlaceId() : null)
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .durationMinutes(entity.getDurationMinutes())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .build();
    }
}