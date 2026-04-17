package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.HourlyRate;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.HourlyRateRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
