package com.drmj.work_tracker.usecase.hourlyRate;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.hourlyRate.HourlyRateStatsResponse;
import com.drmj.work_tracker.entity.HourlyRate;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.HourlyRateService;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetHourlyRateStatsUseCase {
    private final UserOrganizationService userOrganizationService;
    private final HourlyRateService hourlyRateService;
    private final PlaceService placeService;

    public ApiResponse<HourlyRateStatsResponse> execute(UUID placeId) {
        UUID userId = SecurityUtils.getCurrentUserId();
        UUID organizationId = SecurityUtils.getCurrentOrganizationId();

        validateUserBelongsToOrganization(userId, organizationId);
        validatePlaceBelongsToOrganization(placeId, organizationId);

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
                            if (existing.getValidFrom().isAfter(replacement.getValidFrom())) {
                                return existing;
                            }
                            return replacement;
                        }
                ));

        long activeRates = 0;
        long expiringSoon = 0;
        long noRate = 0;

        for (UserOrganization member : members) {
            UUID memberId = member.getUser().getId();
            HourlyRate currentRate = currentRateMap.get(memberId);

            if (currentRate == null) {
                noRate++;
            } else if (currentRate.getValidTo() == null) {
                activeRates++;
            } else if (currentRate.getValidTo().isAfter(soon)) {
                activeRates++;
            } else if (currentRate.getValidTo().isAfter(now)) {
                expiringSoon++;
            } else {
                noRate++;
            }
        }

        HourlyRateStatsResponse stats = HourlyRateStatsResponse.builder()
                .totalEmployees(members.size())
                .activeRates(activeRates)
                .noRate(noRate)
                .expiringSoon(expiringSoon)
                .build();

        return new ApiResponse<>(stats);
    }

    private void validateUserBelongsToOrganization(UUID userId, UUID organizationId) {
        boolean belongs = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!belongs) {
            throw new BusinessException(ErrorMessage.USER_NOT_ACCESS_TO_ORGANIZATION.getMessage());
        }
    }

    private void validatePlaceBelongsToOrganization(UUID placeId, UUID organizationId) {
        Place place = placeService.getById(placeId);
        if (!place.getOrganization().getId().equals(organizationId)) {
            throw new BusinessException(ErrorMessage.PLACE_NOT_IN_ORG.getMessage());
        }
    }
}
