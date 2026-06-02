package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.hourlyRate.CreateHourlyRateRequest;
import com.drmj.work_tracker.dto.request.hourlyRate.UpdateHourlyRateRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.hourlyRate.HourlyRateByPlaceResponse;
import com.drmj.work_tracker.dto.response.hourlyRate.HourlyRateResponse;
import com.drmj.work_tracker.dto.response.hourlyRate.HourlyRateStatsResponse;
import com.drmj.work_tracker.usecase.hourlyRate.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/hourly-rates")
@RequiredArgsConstructor
public class HourlyRateController {
    private final GetHourlyRatesByPlaceUseCase getHourlyRatesByPlaceUseCase;
    private final CreateHourlyRateUseCase createHourlyRateUseCase;
    private final UpdateHourlyRateUseCase updateHourlyRateUseCase;
    private final DeleteHourlyRateUseCase deleteHourlyRateUseCase;
    private final GetHourlyRateStatsUseCase getHourlyRateStatsUseCase;

    @GetMapping("/by-place/{placeId}")
    public ApiResponse<List<HourlyRateByPlaceResponse>> getByPlace(@PathVariable UUID placeId) {
        return getHourlyRatesByPlaceUseCase.execute(placeId);
    }

    @PostMapping
    public ApiResponse<HourlyRateResponse> create(@RequestBody @Valid CreateHourlyRateRequest request) {
        return createHourlyRateUseCase.execute(request);
    }

    @PutMapping("/{id}")
    public ApiResponse<HourlyRateResponse> update(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateHourlyRateRequest request) {
        return updateHourlyRateUseCase.execute(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        return deleteHourlyRateUseCase.execute(id);
    }

    @GetMapping("/stats")
    public ApiResponse<HourlyRateStatsResponse> getStats(@RequestParam UUID placeId) {
        return getHourlyRateStatsUseCase.execute(placeId);
    }
}
