package com.drmj.work_tracker.usecase.hourlyRate;

import com.drmj.work_tracker.dto.request.hourlyRate.CreateHourlyRateRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.hourlyRate.HourlyRateResponse;
import com.drmj.work_tracker.entity.HourlyRate;
import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.HourlyRateService;
import com.drmj.work_tracker.service.OrganizationService;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.service.UserService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateHourlyRateUseCase {
    private final HourlyRateService hourlyRateService;
    private final UserOrganizationService userOrganizationService;
    private final PlaceService placeService;
    private final OrganizationService organizationService;
    private final UserService userService;

    public ApiResponse<HourlyRateResponse> execute(CreateHourlyRateRequest request) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        UUID organizationId = SecurityUtils.getCurrentOrganizationId();

        validateUserBelongsToOrganization(currentUserId, organizationId);
        validateUserRole(currentUserId, organizationId);
        Place place = validatePlaceBelongsToOrganization(request.getPlaceId(), organizationId);
        validateTargetUserBelongsToOrganization(request.getUserId(), organizationId);

        closeOpenEndedActiveRate(request.getUserId(), organizationId, request.getPlaceId());

        HourlyRate hourlyRate = buildHourlyRate(request, organizationId, place);
        HourlyRate saved = hourlyRateService.save(hourlyRate);

        return new ApiResponse<>(HourlyRateResponse.fromEntity(saved));
    }

    private void validateUserBelongsToOrganization(UUID userId, UUID organizationId) {
        boolean belongs = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!belongs) {
            throw new BusinessException(ErrorMessage.USER_NOT_ACCESS_TO_ORGANIZATION.getMessage());
        }
    }

    private void validateUserRole(UUID userId, UUID organizationId) {
        UserOrganization userOrg = userOrganizationService.findByUserIdAndOrganizationId(userId, organizationId);
        UserOrganizationRole role = userOrg.getRole();
        if (role != UserOrganizationRole.ADMIN && role != UserOrganizationRole.EMPLOYER) {
            throw new BusinessException(ErrorMessage.USER_NOT_AUTHORIZED_TO_MANAGE_HOURLY_RATES.getMessage());
        }
    }

    private Place validatePlaceBelongsToOrganization(UUID placeId, UUID organizationId) {
        Place place = placeService.getById(placeId);
        if (!place.getOrganization().getId().equals(organizationId)) {
            throw new BusinessException(ErrorMessage.PLACE_NOT_IN_ORG.getMessage());
        }
        return place;
    }

    private void validateTargetUserBelongsToOrganization(UUID targetUserId, UUID organizationId) {
        boolean belongs = userOrganizationService.existsByUserIdAndOrganizationId(targetUserId, organizationId);
        if (!belongs) {
            throw new BusinessException(ErrorMessage.USER_NOT_IN_ORG.getMessage());
        }
    }

    private void closeOpenEndedActiveRate(UUID userId, UUID organizationId, UUID placeId) {
        OffsetDateTime now = OffsetDateTime.now();
        Optional<HourlyRate> activeRateOpt = hourlyRateService.findActiveRate(userId, organizationId, placeId, now);
        if (activeRateOpt.isPresent()) {
            HourlyRate activeRate = activeRateOpt.get();
            if (activeRate.getValidTo() == null) {
                activeRate.setValidTo(now);
                hourlyRateService.update(activeRate);
            }
        }
    }

    private HourlyRate buildHourlyRate(CreateHourlyRateRequest request, UUID organizationId, Place place) {
        Organization organization = organizationService.getById(organizationId);
        User user = userService.getById(request.getUserId());

        HourlyRate hourlyRate = new HourlyRate();
        hourlyRate.setUser(user);
        hourlyRate.setOrganization(organization);
        hourlyRate.setPlace(place);
        hourlyRate.setRate(request.getRate());
        hourlyRate.setValidFrom(request.getValidFrom());
        hourlyRate.setValidTo(request.getValidTo());
        return hourlyRate;
    }
}
