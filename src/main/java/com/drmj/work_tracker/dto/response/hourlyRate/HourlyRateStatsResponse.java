package com.drmj.work_tracker.dto.response.hourlyRate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HourlyRateStatsResponse {
    private long totalEmployees;
    private long activeRates;
    private long noRate;
    private long expiringSoon;
}
