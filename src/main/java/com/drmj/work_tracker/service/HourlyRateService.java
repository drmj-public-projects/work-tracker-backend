package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.HourlyRate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HourlyRateService {
    HourlyRate getById(UUID id);

    List<HourlyRate> getActiveRatesByPlaceIds(UUID userId, UUID organizationId, List<UUID> placeIds, OffsetDateTime time);

    List<HourlyRate> getAllByOrganizationIdAndPlaceId(UUID organizationId, UUID placeId);

    List<HourlyRate> getAllByUserIdAndOrganizationIdAndPlaceId(UUID userId, UUID organizationId, UUID placeId);

    HourlyRate save(HourlyRate hourlyRate);

    HourlyRate update(HourlyRate hourlyRate);

    void delete(UUID id);

    List<HourlyRate> getExpiringSoonByPlaceId(UUID organizationId, UUID placeId, OffsetDateTime now, OffsetDateTime soonDate);

    Optional<HourlyRate> findActiveRate(UUID userId, UUID organizationId, UUID placeId, OffsetDateTime time);
}
