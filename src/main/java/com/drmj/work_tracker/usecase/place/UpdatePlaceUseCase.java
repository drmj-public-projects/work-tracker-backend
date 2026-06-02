package com.drmj.work_tracker.usecase.place;

import com.drmj.work_tracker.dto.request.place.UpdatePlaceRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.place.PlaceResponse;
import com.drmj.work_tracker.entity.OrganizationSettings;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.OrganizationSettingsService;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdatePlaceUseCase {
    private final PlaceService placeService;
    private final UserOrganizationService userOrganizationService;
    private final OrganizationSettingsService organizationSettingsService;

    @Transactional
    public ApiResponse<PlaceResponse> execute(UUID placeId, UpdatePlaceRequest request) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        
        Place place = placeService.getById(placeId);
        
        validateUserAccess(currentUserId, place.getOrganizationId());
        validatePlaceName(placeId, place.getOrganizationId(), request);
        validateLocationRequirements(place.getOrganizationId(), request);
        
        updatePlaceEntity(place, request);
        Place saved = placeService.save(place);
        
        return new ApiResponse<>(PlaceResponse.buildFromPlace(saved));
    }

    private void validateUserAccess(UUID userId, UUID organizationId) {
        boolean hasAccess = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!hasAccess) {
            throw new BusinessException(ErrorMessage.USER_NOT_IN_ORG.getMessage());
        }
    }

    private void validatePlaceName(UUID placeId, UUID organizationId, UpdatePlaceRequest request) {
        String normalizedName = request.getName().trim();
        if (normalizedName.isEmpty()) {
            throw new BusinessException(ErrorMessage.PLACE_NOT_FOUND_MESSAGE.getMessage());
        }
        
        boolean exists = placeService.existsByNameAndOrganizationIdExcludingId(
                normalizedName,
                organizationId,
                placeId
        );
        if (exists) {
            throw new BusinessException(ErrorMessage.PLACE_ALREADY_EXISTS.getMessage());
        }
    }

    private void validateLocationRequirements(UUID organizationId, UpdatePlaceRequest request) {
        OrganizationSettings settings = organizationSettingsService.getByOrganizationId(organizationId);
        if (Boolean.TRUE.equals(settings.getRequireLocation())) {
            if (request.getLatitude() == null || request.getLongitude() == null) {
                throw new BusinessException(ErrorMessage.LOCATION_REQUIRED_FOR_ORG.getMessage());
            }
        }
    }

    private void updatePlaceEntity(Place place, UpdatePlaceRequest request) {
        place.setName(request.getName().trim());
        place.setDescription(request.getDescription());
        place.setLatitude(request.getLatitude());
        place.setLongitude(request.getLongitude());
        place.setRadiusMeters(request.getRadiusMeters());
    }
}
