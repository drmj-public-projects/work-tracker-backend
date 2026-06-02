package com.drmj.work_tracker.usecase.hourlyRate;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.hourlyRate.HourlyRateByPlaceResponse;
import com.drmj.work_tracker.entity.HourlyRate;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.HourlyRateStatus;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.HourlyRateService;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetHourlyRatesByPlaceUseCase {
    private final UserOrganizationService userOrganizationService;
    private final HourlyRateService hourlyRateService;
    private final PlaceService placeService;

    public ApiResponse<List<HourlyRateByPlaceResponse>> execute(UUID placeId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        UUID organizationId = SecurityUtils.getCurrentOrganizationId();

        validateUserBelongsToOrganization(userId, organizationId);
        Place place = validatePlaceBelongsToOrganization(placeId, organizationId);

        List<UserOrganization> members = userOrganizationService.findByOrganizationId(organizationId);
        List<HourlyRate> allRates = hourlyRateService.getAllByOrganizationIdAndPlaceId(organizationId, placeId);

        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime soon = now.plusDays(7);

        Map<UUID, HourlyRate> currentRateMap = allRates.stream()
                .filter(hr -> !hr.getValidFrom().isAfter(now))
                .filter(hr -> hr.getValidTo() == null || !hr.getValidTo().isBefore(now))
                .collect(Collectors.toMap(
                        HourlyRate::getUserId,
                        hr -> hr,
                        (existing, replacement) -> {
                            // Prefer the one with later validFrom, or non-null validTo
                            if (existing.getValidFrom().isAfter(replacement.getValidFrom())) {
                                return existing;
                            }
                            return replacement;
                        }
                ));

        List<HourlyRateByPlaceResponse> responses = new ArrayList<>();
        for (UserOrganization member : members) {
            UUID memberId = member.getUser().getId();
            HourlyRate currentRate = currentRateMap.get(memberId);
            HourlyRateStatus status;

            if (currentRate == null) {
                status = HourlyRateStatus.NO_RATE;
            } else if (currentRate.getValidTo() == null) {
                status = HourlyRateStatus.ACTIVE;
            } else if (currentRate.getValidTo().isAfter(soon)) {
                status = HourlyRateStatus.ACTIVE;
            } else if (currentRate.getValidTo().isAfter(now)) {
                status = HourlyRateStatus.EXPIRING_SOON;
            } else {
                status = HourlyRateStatus.EXPIRED;
            }

            responses.add(HourlyRateByPlaceResponse.builder()
                    .userId(memberId)
                    .userName(member.getUser().getName())
                    .userEmail(member.getUser().getEmail())
                    .placeId(placeId)
                    .placeName(place.getName())
                    .rateId(currentRate != null ? currentRate.getId() : null)
                    .rate(currentRate != null ? currentRate.getRate() : null)
                    .validFrom(currentRate != null ? currentRate.getValidFrom() : null)
                    .validTo(currentRate != null ? currentRate.getValidTo() : null)
                    .status(status)
                    .build());
        }

        return new ApiResponse<>(responses);
    }

    private void validateUserBelongsToOrganization(UUID userId, UUID organizationId) {
        boolean belongs = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!belongs) {
            throw new BusinessException(ErrorMessage.USER_NOT_ACCESS_TO_ORGANIZATION.getMessage());
        }
    }

    private Place validatePlaceBelongsToOrganization(UUID placeId, UUID organizationId) {
        Place place = placeService.getById(placeId);
        if (!place.getOrganization().getId().equals(organizationId)) {
            throw new BusinessException(ErrorMessage.PLACE_NOT_IN_ORG.getMessage());
        }
        return place;
    }
}
