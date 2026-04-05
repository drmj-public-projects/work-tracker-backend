package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.request.workSession.CreateManualWorkSessionRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.entity.OrganizationSettings;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.service.OrganizationSettingsService;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.service.WorkSessionService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateManualWorkSessionUseCase {
    private final PlaceService placeService;
    private final WorkSessionService workSessionService;
    private final OrganizationSettingsService settingsService;
    private final UserOrganizationService userOrganizationService;

    public ApiResponse<WorkSessionResponse> execute(CreateManualWorkSessionRequest request) {
        validate(request);
        WorkSession session = workSessionService.createManualSession(request);
        return new ApiResponse<>(WorkSessionResponse.fromEntity(session));
    }

    private void validate(CreateManualWorkSessionRequest request) {
        if (request.getEndTime().isBefore(request.getStartTime())) {
            throw new BusinessException(ErrorMessage.INVALID_TIME_RANGE.getMessage());
        }

        boolean userBelongsToOrganization = userOrganizationService.existsByUserIdAndOrganizationId(
                request.getUserId(), request.getOrganizationId());
        if (!userBelongsToOrganization) {
            throw new BusinessException(ErrorMessage.USER_NOT_IN_ORG.getMessage());
        }

        OrganizationSettings settings = settingsService.getByOrganizationId(request.getOrganizationId());
        if (!settings.getAllowManualEntries()) {
            throw new BusinessException(ErrorMessage.MANUAL_NOT_ALLOWED.getMessage());
        }
        if (settings.getRequireLocation()) {
            if (request.getLatitude() == null || request.getLongitude() == null) {
                throw new BusinessException(ErrorMessage.LOCATION_REQUIRED.getMessage());
            }
        }

        Place place = placeService.getById(request.getPlaceId());
        if (!place.getOrganizationId().equals(request.getOrganizationId())) {
            throw new BusinessException(ErrorMessage.PLACE_NOT_IN_ORG.getMessage());
        }

        boolean overlaps = workSessionService.existsOverlappingSession(
                request.getUserId(),
                request.getStartTime(),
                request.getEndTime()
        );
        if (overlaps) {
            throw new BusinessException(ErrorMessage.SESSION_OVERLAP.getMessage());
        }
    }
}
