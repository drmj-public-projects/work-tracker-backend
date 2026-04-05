package com.drmj.work_tracker.usecase.place;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.place.PlaceResponse;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.service.OrganizationService;
import com.drmj.work_tracker.service.PlaceService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPlacesByOrganizationIdUseCase {
    private final PlaceService placeService;
    private final OrganizationService organizationService;

    public ApiResponse<List<PlaceResponse>> execute(UUID organizationId) {
        this.validateOrganization(organizationId);
        List<Place> placeList =  placeService.getAllByOrganizationId(organizationId);
        return new ApiResponse<>(
                placeList.stream()
                        .map(PlaceResponse::buildFromPlace)
                        .collect(Collectors.toList())
        );
    }

    private void validateOrganization(UUID organizationId) {
        boolean organizationExists = organizationService.validateIfExists(organizationId);
        if(Boolean.FALSE.equals(organizationExists)) {
            throw new NotFoundException(ErrorMessage.ORGANIZATION_NOT_FOUND_MESSAGE.getMessage());
        }
    }
}
