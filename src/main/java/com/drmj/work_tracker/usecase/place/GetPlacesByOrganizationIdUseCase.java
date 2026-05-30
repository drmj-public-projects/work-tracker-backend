package com.drmj.work_tracker.usecase.place;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.place.PlaceDetailResponse;
import com.drmj.work_tracker.entity.HourlyRate;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.service.HourlyRateService;
import com.drmj.work_tracker.service.OrganizationService;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPlacesByOrganizationIdUseCase {
    private final PlaceService placeService;
    private final OrganizationService organizationService;
    private final HourlyRateService hourlyRateService;
    private final UserOrganizationService userOrganizationService;

    public ApiResponse<List<PlaceDetailResponse>> execute(UUID organizationId, UUID userId) {
        this.validateOrganization(organizationId);
        this.validateUserBelongsToOrganization(userId, organizationId);

        List<Place> placeList = placeService.getAllByOrganizationId(organizationId);
        List<UUID> placeIds = placeList.stream()
                .map(Place::getId)
                .collect(Collectors.toList());

        final Map<UUID, BigDecimal> hourlyRateMap = new java.util.HashMap<>();
        if (!placeIds.isEmpty()) {
            List<HourlyRate> hourlyRates = hourlyRateService.getActiveRatesByPlaceIds(
                    userId,
                    organizationId,
                    placeIds,
                    OffsetDateTime.now()
            );
            hourlyRateMap.putAll(hourlyRates.stream()
                    .collect(Collectors.toMap(
                            HourlyRate::getPlaceId,
                            HourlyRate::getRate,
                            (existing, replacement) -> existing
                    )));
        }

        List<PlaceDetailResponse> response = placeList.stream()
                .map(place -> PlaceDetailResponse.fromPlaceAndRate(
                        place,
                        hourlyRateMap.get(place.getId())
                ))
                .collect(Collectors.toList());

        return new ApiResponse<>(response);
    }

    private void validateOrganization(UUID organizationId) {
        boolean organizationExists = organizationService.validateIfExists(organizationId);
        if (Boolean.FALSE.equals(organizationExists)) {
            throw new NotFoundException(ErrorMessage.ORGANIZATION_NOT_FOUND_MESSAGE.getMessage());
        }
    }

    private void validateUserBelongsToOrganization(UUID userId, UUID organizationId) {
        boolean userBelongsToOrganization = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!userBelongsToOrganization) {
            throw new BusinessException(ErrorMessage.USER_NOT_ACCESS_TO_ORGANIZATION.getMessage());
        }
    }
}
