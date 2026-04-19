package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.workSession.CreateManualWorkSessionRequest;
import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.dto.request.workSession.UpdateWorkSessionRequest;
import com.drmj.work_tracker.dto.request.workSession.WorkSessionPlaceSummaryQuery;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionSummaryResponse;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import com.drmj.work_tracker.usecase.workSession.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workSessions")
@RequiredArgsConstructor
public class WorkSessionController {
    private final StartWorkSessionUseCase startWorkSessionUseCase;
    private final EndWorkSessionUseCase endWorkSessionUseCase;
    private final CreateManualWorkSessionUseCase createManualWorkSessionUseCase;
    private final UpdateWorkSessionUseCase updateWorkSessionUseCase;
    private final GetWorkSessionByPlaceIdUseCase getWorkSessionByPlaceIdUseCase;

    @PostMapping("/start")
    public ApiResponse<WorkSessionResponse> start(
            @RequestBody @Valid StartWorkSessionRequest request
    ) {
        return startWorkSessionUseCase.execute(request);
    }

    @PostMapping("/{id}/end")
    public ApiResponse<WorkSessionResponse> endSession(
            @PathVariable UUID id,
            @RequestParam UUID userId
    ) {
        return endWorkSessionUseCase.execute(id, userId);
    }

    @PostMapping("/manual")
    public ApiResponse<WorkSessionResponse> createManual(
            @RequestBody @Valid CreateManualWorkSessionRequest request
    ) {
        return createManualWorkSessionUseCase.execute(request);
    }

    @PutMapping("/{id}")
    public ApiResponse<WorkSessionResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateWorkSessionRequest request,
            @RequestParam UUID currentUserId
    ) {
        return updateWorkSessionUseCase.execute(id, request, currentUserId);
    }

    @GetMapping("/summaryByPlaceId")
    public ApiResponse<WorkSessionSummaryResponse> getSummary(
            @RequestParam UUID placeId,
            @RequestParam String range,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) List<WorkSessionStatus> status,
            @RequestParam(required = false) OffsetDateTime startDate,
            @RequestParam(required = false) OffsetDateTime endDate,
            @RequestParam UUID userId
    ) {
        WorkSessionPlaceSummaryQuery query = WorkSessionPlaceSummaryQuery.builder()
                .placeId(placeId)
                .userId(userId)
                .range(range)
                .groupBy(groupBy)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        return getWorkSessionByPlaceIdUseCase.execute(query);
      }
}
