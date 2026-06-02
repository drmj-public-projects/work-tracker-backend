package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.HourlyRate;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.HourlyRateRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HourlyRateServiceImpl implements HourlyRateService {
    private final HourlyRateRepository hourlyRateRepository;

    @Override
    public HourlyRate getById(UUID id) {
        return hourlyRateRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.HOURLY_RATE_NOT_FOUND_MESSAGE.getMessage()));
    }

    @Override
    public List<HourlyRate> getActiveRatesByPlaceIds(UUID userId, UUID organizationId, List<UUID> placeIds, OffsetDateTime time) {
        return hourlyRateRepository.findActiveRatesByPlaceIds(userId, organizationId, placeIds, time);
    }

    @Override
    public List<HourlyRate> getAllByOrganizationIdAndPlaceId(UUID organizationId, UUID placeId) {
        return hourlyRateRepository.findAllByOrganizationIdAndPlaceId(organizationId, placeId);
    }

    @Override
    public List<HourlyRate> getAllByUserIdAndOrganizationIdAndPlaceId(UUID userId, UUID organizationId, UUID placeId) {
        return hourlyRateRepository.findAllByUserIdAndOrganizationIdAndPlaceId(userId, organizationId, placeId);
    }

    @Override
    public HourlyRate save(HourlyRate hourlyRate) {
        return hourlyRateRepository.save(hourlyRate);
    }

    @Override
    public HourlyRate update(HourlyRate hourlyRate) {
        return hourlyRateRepository.save(hourlyRate);
    }

    @Override
    public void delete(UUID id) {
        hourlyRateRepository.deleteById(id);
    }

    @Override
    public List<HourlyRate> getExpiringSoonByPlaceId(UUID organizationId, UUID placeId, OffsetDateTime now, OffsetDateTime soonDate) {
        return hourlyRateRepository.findExpiringSoonByPlaceId(organizationId, placeId, now, soonDate);
    }

    @Override
    public Optional<HourlyRate> findActiveRate(UUID userId, UUID organizationId, UUID placeId, OffsetDateTime time) {
        return hourlyRateRepository.findActiveRate(userId, organizationId, placeId, time);
    }
}
