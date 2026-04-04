package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.PlaceRepository;
import com.drmj.work_tracker.utils.ApiResponseConstants;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;

    @Override
    public Place getById(UUID id) {
        Optional<Place> optionalPlace = placeRepository.findById(id);
        if (optionalPlace.isEmpty()) {
            throw new NotFoundException(ErrorMessage.PLACE_NOT_FOUND_MESSAGE.getMessage());
        }
        return optionalPlace.get();
    }

    @Override
    public List<Place> getAllByOrganizationId(UUID organizationId) {
        return placeRepository.findByOrganization_id(organizationId);
    }
}
