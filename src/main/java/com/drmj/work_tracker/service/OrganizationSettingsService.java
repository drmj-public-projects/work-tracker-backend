package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.OrganizationSettings;

import java.util.UUID;

public interface OrganizationSettingsService {
    OrganizationSettings getByOrganizationId(UUID id);
    OrganizationSettings save(OrganizationSettings settings);

    java.util.Optional<OrganizationSettings> findByOrganizationId(UUID organizationId);
}
