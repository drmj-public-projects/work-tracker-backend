package com.drmj.work_tracker.dto.response.workSession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WorkSessionSummaryResponse {
    private UUID placeId;
    private String placeName;
    private String groupBy;   // DAY, WEEK, MONTH
    private String range;     // THIS_WEEK, etc.
    private List<WorkSessionSummary> summaryBlocks;
}
