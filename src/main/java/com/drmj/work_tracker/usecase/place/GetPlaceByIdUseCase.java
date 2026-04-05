package com.drmj.work_tracker.usecase.place;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.place.PlaceResponse;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPlaceByIdUseCase {
    private final PlaceService placeService;

    public ApiResponse<PlaceResponse> execute(UUID id) {
        Place entity = placeService.getById(id);
        return new ApiResponse<>(PlaceResponse.buildFromPlace(entity));
    }
}
