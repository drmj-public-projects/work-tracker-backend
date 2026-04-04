package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.OrganizationSettings;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.OrganizationSettingsRepository;
import com.drmj.work_tracker.utils.ApiResponseConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationSettingsServiceImpl implements OrganizationSettingsService {
    private final OrganizationSettingsRepository organizationSettingsRepository;

    @Override
    public OrganizationSettings getByOrganizationId(UUID id) {
        Optional<OrganizationSettings> optionalOrganizationSettings = organizationSettingsRepository.getByOrganizationId(id);
        if (optionalOrganizationSettings.isEmpty()) {
            throw new NotFoundException(ApiResponseConstants.NOT_FOUND_MESSAGE);
        }
        return optionalOrganizationSettings.get();
    }
}
