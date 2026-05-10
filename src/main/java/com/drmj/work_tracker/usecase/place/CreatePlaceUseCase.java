package com.drmj.work_tracker.usecase.place;

import com.drmj.work_tracker.dto.request.place.CreatePlaceRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.place.PlaceResponse;
import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.OrganizationSettings;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.OrganizationService;
import com.drmj.work_tracker.service.OrganizationSettingsService;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreatePlaceUseCase {
    private final OrganizationService organizationService;
    private final OrganizationSettingsService organizationSettingsService;
    private final PlaceService placeService;
    private final UserOrganizationService userOrganizationService;

    public ApiResponse<PlaceResponse> execute(CreatePlaceRequest request) {
        validateUserAccess(SecurityUtils.getCurrentUserId(), request.getOrganizationId());
        validatePlaceName(request);
        validateLocationRequirements(request);
        Place place = buildPlaceEntity(request);
        Place saved = placeService.save(place);
        return new ApiResponse<>(PlaceResponse.buildFromPlace(saved));
    }

    private void validateUserAccess(UUID userId, UUID organizationId) {
        boolean hasAccess = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!hasAccess) {
            throw new BusinessException(ErrorMessage.USER_NOT_IN_ORG.getMessage());
        }
    }

    private void validatePlaceName(CreatePlaceRequest request) {
        String normalizedName = request.getName().trim();
        if (normalizedName.isEmpty()) {
            throw new BusinessException(ErrorMessage.PLACE_NOT_FOUND_MESSAGE.getMessage());
        }
        boolean exists = placeService.existsByNameAndOrganizationId(
                normalizedName, 
                request.getOrganizationId()
        );
        if (exists) {
            throw new BusinessException(ErrorMessage.PLACE_ALREADY_EXISTS.getMessage());
        }
    }

    private void validateLocationRequirements(CreatePlaceRequest request) {
        OrganizationSettings settings = organizationSettingsService
                .getByOrganizationId(request.getOrganizationId());
        if (Boolean.TRUE.equals(settings.getRequireLocation())) {
            if (request.getLatitude() == null || request.getLongitude() == null) {
                throw new BusinessException(ErrorMessage.LOCATION_REQUIRED_FOR_ORG.getMessage());
            }
        }
    }

    private Place buildPlaceEntity(CreatePlaceRequest request) {
        Organization organization = organizationService.getById(request.getOrganizationId());
        return Place.builder()
                .organization(organization)
                .name(request.getName().trim())
                .description(request.getDescription())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .radiusMeters(request.getRadiusMeters())
                .isActive(true)
                .build();
    }
}