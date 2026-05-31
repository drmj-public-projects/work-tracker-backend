package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.workSession.*;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionPlaceResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionSummaryResponse;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import com.drmj.work_tracker.usecase.workSession.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workSessions")
@RequiredArgsConstructor
public class WorkSessionController {
    private final StartWorkSessionUseCase startWorkSessionUseCase;
    private final EndWorkSessionUseCase endWorkSessionUseCase;
    private final CreateManualWorkSessionUseCase createManualWorkSessionUseCase;
    private final UpdateWorkSessionUseCase updateWorkSessionUseCase;
    private final GetWorkSessionSummaryUseCase getWorkSessionSummaryUseCase;
    private final GetWorkSessionDetailUseCase getWorkSessionDetailUseCase;
    private final GetCurrentWorkSessionUseCase getCurrentWorkSessionUseCase;
    private final UpdateActiveWorkSessionUseCase updateActiveWorkSessionUseCase;

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
            @RequestBody @Valid UpdateWorkSessionRequest request
    ) {
        return updateWorkSessionUseCase.execute(id, request);
    }

    @GetMapping("/summary")
    public ApiResponse<WorkSessionSummaryResponse> getSummary(
            @RequestParam(required = false) UUID placeId,
            @RequestParam(required = false) UUID organizationId,
            @RequestParam String range,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) List<WorkSessionStatus> status,
            @RequestParam(required = false) OffsetDateTime startDate,
            @RequestParam(required = false) OffsetDateTime endDate,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        WorkSessionPlaceSummaryQuery query = WorkSessionPlaceSummaryQuery.builder()
                .placeId(placeId)
                .organizationId(organizationId)
                .userId(userId)
                .range(range)
                .groupBy(groupBy)
                .status(status)
                .startDate(startDate)
                .endDate(endDate)
                .build();
        return getWorkSessionSummaryUseCase.execute(query);
    }

    @GetMapping()
    public ApiResponse<Page<WorkSessionResponse>> getWorkSessions(
            @RequestParam(required = false) UUID placeId,
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(required = false) String range,
            @RequestParam(required = false) OffsetDateTime startDate,
            @RequestParam(required = false) OffsetDateTime endDate,
            @RequestParam(required = false) List<WorkSessionStatus> status,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            Authentication authentication
    ) {
        UUID userId = UUID.fromString(authentication.getName());
        WorkSessionDetailQuery query = WorkSessionDetailQuery.builder()
                .placeId(placeId)
                .organizationId(organizationId)
                .range(range)
                .startDate(startDate)
                .endDate(endDate)
                .status(status)
                .userId(userId)
                .page(page)
                .size(size)
                .build();
        return getWorkSessionDetailUseCase.execute(query);
    }

    @GetMapping("/getCurrentWorkSession")
    public ApiResponse<WorkSessionPlaceResponse> getCurrentWorkSession() {
        return getCurrentWorkSessionUseCase.execute();
    }

    @PatchMapping("/active")
    public ApiResponse<WorkSessionResponse> updateActive(
            @RequestBody @Valid UpdateActiveWorkSessionRequest request
    ) {
        return updateActiveWorkSessionUseCase.execute(request);
    }
}
