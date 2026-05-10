package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.place.CreatePlaceRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.place.PlaceResponse;
import com.drmj.work_tracker.usecase.place.CreatePlaceUseCase;
import com.drmj.work_tracker.usecase.place.GetPlaceByIdUseCase;
import com.drmj.work_tracker.usecase.place.GetPlacesByOrganizationIdUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {
    private final GetPlaceByIdUseCase getPlaceByIdUseCase;
    private final CreatePlaceUseCase createPlaceUseCase;
    private final GetPlacesByOrganizationIdUseCase getPlacesByOrganizationIdUseCase;

    @GetMapping("/{id}")
    public ApiResponse<PlaceResponse> getById(@PathVariable("id") UUID id) {
        return getPlaceByIdUseCase.execute(id);
    }

    @GetMapping("/getByOrganizationId/{organizationId}")
    public ApiResponse<List<PlaceResponse>> getByOrganizationId(@PathVariable("organizationId") UUID organizationId) {
        return getPlacesByOrganizationIdUseCase.execute(organizationId);
    }

    @PostMapping
    public ApiResponse<PlaceResponse> create(@RequestBody @Valid CreatePlaceRequest request) {
        return createPlaceUseCase.execute(request);
    }
}
