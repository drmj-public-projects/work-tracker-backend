package com.drmj.work_tracker.dto.response.workSession;

import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.utils.Utils;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSessionPlaceResponse {
    private UUID id;
    private UUID userId;
    private UUID organizationId;
    private UUID placeId;
    private String placeName;
    private Double placeLatitude;
    private Double placeLongitude;
    private Integer placeRadiusMeters;
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

    public static WorkSessionPlaceResponse fromEntity(WorkSession entity, Place place) {
        return WorkSessionPlaceResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .organizationId(entity.getOrganizationId())
                .placeId(entity.getPlaceId())
                .placeName(place != null ? place.getName() : null)
                .placeLatitude(place != null ? place.getLatitude() : null)
                .placeLongitude(place != null ? place.getLongitude() : null)
                .placeRadiusMeters(place != null ? place.getRadiusMeters() : null)
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
