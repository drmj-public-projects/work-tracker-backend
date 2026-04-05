package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.workSession.CreateManualWorkSessionRequest;
import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.dto.request.workSession.UpdateWorkSessionRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.usecase.workSession.CreateManualWorkSessionUseCase;
import com.drmj.work_tracker.usecase.workSession.EndWorkSessionUseCase;
import com.drmj.work_tracker.usecase.workSession.StartWorkSessionUseCase;
import com.drmj.work_tracker.usecase.workSession.UpdateWorkSessionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workSessions")
@RequiredArgsConstructor
public class WorkSessionController {
    private final StartWorkSessionUseCase startWorkSessionUseCase;
    private final EndWorkSessionUseCase endWorkSessionUseCase;
    private final CreateManualWorkSessionUseCase createManualWorkSessionUseCase;
    private final UpdateWorkSessionUseCase updateWorkSessionUseCase;

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
}
