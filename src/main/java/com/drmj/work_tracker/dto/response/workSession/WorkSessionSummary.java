package com.drmj.work_tracker.dto.response.workSession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WorkSessionSummary {
    private String periodLabel;     // "2026-04-17", "2026-W15", "2026-04"
    private Integer totalSessions;
    private Integer totalMinutes;
    private BigDecimal totalPay;
    private Integer timerMinutes;
    private BigDecimal timerPay;
    private Integer manualMinutes;
    private BigDecimal manualPay;
}
