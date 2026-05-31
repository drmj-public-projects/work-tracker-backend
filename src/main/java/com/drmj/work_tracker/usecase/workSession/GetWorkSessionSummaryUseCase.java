package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.request.workSession.WorkSessionPlaceSummaryQuery;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.*;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionEntryType;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.WorkSessionService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetWorkSessionSummaryUseCase {
    private final PlaceService placeService;
    private final WorkSessionService workSessionService;

    public ApiResponse<WorkSessionSummaryResponse> execute(WorkSessionPlaceSummaryQuery query) {
        validate(query);
        WorkSessionRange rangeEnum = WorkSessionRange.from(query.getRange());
        WorkSessionGroupBy groupBy = WorkSessionGroupBy.from(query.getGroupBy());
        DateRange range = rangeEnum.resolve(
                OffsetDateTime.now(),
                query.getStartDate(),
                query.getEndDate()
        );
        List<WorkSession> sessions = workSessionService.findByFilters(
                query.getPlaceId(),
                query.getOrganizationId(),
                range.getStart(),
                range.getEnd(),
                query.getStatus(),
                query.getUserId()
        );
        return buildResponse(
                query.getPlaceId(),
                query.getOrganizationId(),
                sessions,
                groupBy,
                rangeEnum
        );
    }

    private void validate(WorkSessionPlaceSummaryQuery query) {
        if (query.getPlaceId() == null && query.getOrganizationId() == null) {
            throw new IllegalArgumentException(ErrorMessage.PLACE_ID_OR_ORGANIZATION_ID_REQUIRED.getMessage());
        }
        if (query.getPlaceId() != null && query.getOrganizationId() != null) {
            throw new IllegalArgumentException(ErrorMessage.ONLY_ONE_OF_PLACE_OR_ORGANIZATION_ALLOWED.getMessage());
        }
    }

    private ApiResponse<WorkSessionSummaryResponse> buildResponse(
            UUID placeId,
            UUID organizationId,
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

                    int timerMinutes = filterAndSumMinutes(group, WorkSessionEntryType.TIMER);
                    BigDecimal timerPay = filterAndSumPay(group, WorkSessionEntryType.TIMER);
                    int manualMinutes = filterAndSumMinutes(group, WorkSessionEntryType.MANUAL);
                    BigDecimal manualPay = filterAndSumPay(group, WorkSessionEntryType.MANUAL);

                    return WorkSessionSummary.builder()
                            .periodLabel(entry.getKey())
                            .totalSessions(totalSessions)
                            .totalMinutes(totalMinutes)
                            .totalPay(totalPay)
                            .timerMinutes(timerMinutes)
                            .timerPay(timerPay)
                            .manualMinutes(manualMinutes)
                            .manualPay(manualPay)
                            .build();
                })
                .sorted(Comparator.comparing(WorkSessionSummary::getPeriodLabel))
                .toList();

        String placeName;
        if (placeId != null) {
            placeName = placeService.getById(placeId).getName();
        } else {
            placeName = "All Places";
        }

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

    private int filterAndSumMinutes(List<WorkSession> sessions, WorkSessionEntryType entryType) {
        return sessions.stream()
                .filter(ws -> ws.getEntryType() == entryType)
                .map(ws -> ws.getDurationMinutes() == null ? 0 : ws.getDurationMinutes())
                .reduce(0, Integer::sum);
    }

    private BigDecimal filterAndSumPay(List<WorkSession> sessions, WorkSessionEntryType entryType) {
        return sessions.stream()
                .filter(ws -> ws.getEntryType() == entryType)
                .map(ws -> ws.getTotalPay() == null ? BigDecimal.ZERO : ws.getTotalPay())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
