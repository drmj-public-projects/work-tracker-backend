package com.drmj.work_tracker.dto.response.hourlyRate;

import com.drmj.work_tracker.entity.enums.HourlyRateStatus;
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
public class HourlyRateByPlaceResponse {
    private UUID userId;
    private String userName;
    private String userEmail;
    private UUID placeId;
    private String placeName;
    private UUID rateId;
    private BigDecimal rate;
    private OffsetDateTime validFrom;
    private OffsetDateTime validTo;
    private HourlyRateStatus status;
}
