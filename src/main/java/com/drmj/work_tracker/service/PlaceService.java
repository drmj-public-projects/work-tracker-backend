package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.Place;

import java.util.UUID;

public interface PlaceService {
    Place getById(UUID id);
}
