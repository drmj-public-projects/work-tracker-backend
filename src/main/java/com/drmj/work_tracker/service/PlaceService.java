package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.Place;

import java.util.List;
import java.util.UUID;

public interface PlaceService {
    Place getById(UUID id);

    List<Place> getAllByOrganizationId(UUID organizationId);

    Place save(Place place);

    boolean existsByNameAndOrganizationId(String name, UUID organizationId);

    boolean existsByNameAndOrganizationIdExcludingId(String name, UUID organizationId, UUID id);
}
