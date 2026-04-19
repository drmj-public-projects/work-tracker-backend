package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.request.workSession.WorkSessionPlaceSummaryQuery;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.*;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.WorkSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetWorkSessionByPlaceIdUseCase {
    private final PlaceService placeService;
    private final WorkSessionService workSessionService;

    public ApiResponse<WorkSessionSummaryResponse> execute(WorkSessionPlaceSummaryQuery query) {
        WorkSessionRange rangeEnum = WorkSessionRange.from(query.getRange());
        WorkSessionGroupBy groupBy = WorkSessionGroupBy.from(query.getGroupBy());
        DateRange range = rangeEnum.resolve(
                OffsetDateTime.now(),
                query.getStartDate(),
                query.getEndDate()
        );
        List<WorkSession> sessions = workSessionService.findByFilters(
                query.getPlaceId(),
                range.getStart(),
                range.getEnd(),
                query.getStatus(),
                query.getUserId()
        );
        return buildResponse(
                query.getPlaceId(),
                sessions,
                groupBy,
                rangeEnum
        );
    }

    private ApiResponse<WorkSessionSummaryResponse> buildResponse(
            UUID placeId,
            List<WorkSession> sessions,
            WorkSessionGroupBy groupBy,
            WorkSessionRange rangeEnum
    ) {
        Map<String, List<WorkSession>> grouped = sessions.stream()
                .collect(Collectors.groupingBy(ws -> groupBy.getKey(ws.getStartTime())));
        List<WorkSessionSummary> blocks = grouped.entrySet().stream()
                .map(entry -> {
                    List<WorkSession> group = entry.getValue();
                    int totalSessions = group.size();
                    int totalMinutes = group.stream()
                            .map(ws -> ws.getDurationMinutes() == null ? 0 : ws.getDurationMinutes())
                            .reduce(0, Integer::sum);
                    BigDecimal totalPay = group.stream()
                            .map(ws -> ws.getTotalPay() == null ? BigDecimal.ZERO : ws.getTotalPay())
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return WorkSessionSummary.builder()
                            .periodLabel(entry.getKey())
                            .totalSessions(totalSessions)
                            .totalMinutes(totalMinutes)
                            .totalPay(totalPay)
                            .build();
                })
                .sorted(Comparator.comparing(WorkSessionSummary::getPeriodLabel))
                .toList();
        String placeName = placeService.getById(placeId).getName();
        return new ApiResponse<>(
                WorkSessionSummaryResponse.builder()
                        .placeId(placeId)
                        .placeName(placeName)
                        .groupBy(groupBy.name().toLowerCase())
                        .range(rangeEnum.name().toLowerCase())
                        .summaryBlocks(blocks)
                        .build()
        );
    }
}