package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionPlaceResponse;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.WorkSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCurrentWorkSessionUseCase {
    private final WorkSessionService workSessionService;
    private final PlaceService placeService;

    public ApiResponse<WorkSessionPlaceResponse> execute() {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<WorkSession> activeSession = workSessionService.findActiveSessionByUserId(userId);
        if (activeSession.isEmpty()) {
            return new ApiResponse<>(null);
        }

        WorkSession session = activeSession.get();
        Place place = placeService.getById(session.getPlaceId());
        return new ApiResponse<>(WorkSessionPlaceResponse.fromEntity(session, place));
    }
}
