package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.HourlyRate;

import java.util.UUID;

public interface HourlyRateService {
    HourlyRate getById(UUID id);
}
