package com.drmj.work_tracker.dto.request.workSession;

import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class WorkSessionDetailQuery {
    private UUID placeId;
    private UUID organizationId;
    private String range;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private List<WorkSessionStatus> status;
    private UUID userId;
    private Integer page;
    private Integer size;
}
