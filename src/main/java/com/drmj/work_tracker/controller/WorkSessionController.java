package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.usecase.workSession.StartWorkSessionUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workSessions")
@RequiredArgsConstructor
public class WorkSessionController {
    private final StartWorkSessionUseCase startWorkSessionUseCase;

    @PostMapping("/start")
    public ApiResponse<WorkSessionResponse> start(
            @RequestBody @Valid StartWorkSessionRequest request
    ) {
        return startWorkSessionUseCase.execute(request);
    }
}
