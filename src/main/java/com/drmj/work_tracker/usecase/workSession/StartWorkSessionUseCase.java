package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.entity.OrganizationSettings;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.service.*;
import com.drmj.work_tracker.utils.ApiResponseConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartWorkSessionUseCase {
    private final PlaceService placeService;
    private final WorkSessionService workSessionService;
    private final UserOrganizationService userOrganizationService;
    private final OrganizationSettingsService organizationSettingsService;

    public ApiResponse<WorkSessionResponse> execute(StartWorkSessionRequest request) {
        this.validateRequest(request);
        WorkSession workSession = workSessionService.startSession(request);

        return new ApiResponse<>(
                ApiResponseConstants.SUCCESS_CODE,
                ApiResponseConstants.SUCCESS_MESSAGE,
                WorkSessionResponse.fromEntity(workSession)
        );
    }

    public void validateRequest(StartWorkSessionRequest request) {
        boolean userBelongsToOrganization = userOrganizationService.existsByUserIdAndOrganizationId(
                request.getUserId(),
                request.getOrganizationId()
        );
        if (!userBelongsToOrganization) {
            throw new BusinessException("User does not belong to organization");
        }

        OrganizationSettings settings = organizationSettingsService.getByOrganizationId(request.getOrganizationId());
        if (settings.getRequireLocation()) {
            if (request.getLatitude() == null || request.getLongitude() == null) {
                throw new BusinessException("Location is required");
            }
        }

        Place place = placeService.getById(request.getPlaceId());
        if (!place.getOrganization().getId().equals(request.getOrganizationId())) {
            throw new BusinessException("Place does not belong to organization");
        }
    }
}
