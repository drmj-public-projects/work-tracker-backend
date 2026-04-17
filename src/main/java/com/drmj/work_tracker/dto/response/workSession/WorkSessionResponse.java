package com.drmj.work_tracker.dto.response.workSession;

import com.drmj.work_tracker.entity.WorkSession;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
    private Integer breakMinutes;
    private BigDecimal hourlyRate;
    private BigDecimal totalPay;
    private String notes;
    private String status;
    private String entryType;
    private String source;

    public static WorkSessionResponse fromEntity(WorkSession entity) {
        return WorkSessionResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .organizationId(entity.getOrganizationId())
                .placeId(entity.getPlaceId())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .durationMinutes(entity.getDurationMinutes())
                .breakMinutes(entity.getBreakMinutes())
                .hourlyRate(entity.getHourlyRate())
                .totalPay(entity.getTotalPay())
                .notes(entity.getNotes())
                .status(entity.getStatus() != null ? entity.getStatus().name() : null)
                .entryType(entity.getEntryType() != null ? entity.getEntryType().name() : null)
                .source(entity.getSource() != null ? entity.getSource().name() : null)
                .build();
    }
}