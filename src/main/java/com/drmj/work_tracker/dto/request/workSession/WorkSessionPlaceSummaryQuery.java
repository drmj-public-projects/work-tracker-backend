package com.drmj.work_tracker.dto.request.workSession;

import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class WorkSessionPlaceSummaryQuery {
    private UUID placeId;
    private UUID userId;
    private String range;
    private String groupBy;
    private List<WorkSessionStatus> status;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
}
