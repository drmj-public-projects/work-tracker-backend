package com.drmj.work_tracker.dto.response.hourlyRate;

import com.drmj.work_tracker.entity.HourlyRate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRateResponse {
    private UUID id;
    private UUID userId;
    private UUID placeId;
    private BigDecimal rate;
    private OffsetDateTime validFrom;
    private OffsetDateTime validTo;

    public static HourlyRateResponse fromEntity(HourlyRate entity) {
        return HourlyRateResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .placeId(entity.getPlaceId())
                .rate(entity.getRate())
                .validFrom(entity.getValidFrom())
                .validTo(entity.getValidTo())
                .build();
    }
}
