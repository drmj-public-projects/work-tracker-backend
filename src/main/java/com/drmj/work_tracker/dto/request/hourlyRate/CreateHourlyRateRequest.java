package com.drmj.work_tracker.dto.request.hourlyRate;

import jakarta.validation.constraints.*;
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
public class CreateHourlyRateRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID placeId;

    @NotNull
    @DecimalMin(value = "0.01")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal rate;

    @NotNull
    private OffsetDateTime validFrom;

    private OffsetDateTime validTo;
}
