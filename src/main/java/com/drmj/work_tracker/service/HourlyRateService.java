package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.HourlyRate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface HourlyRateService {
    HourlyRate getById(UUID id);

    List<HourlyRate> getActiveRatesByPlaceIds(UUID userId, UUID organizationId, List<UUID> placeIds, OffsetDateTime time);
}
