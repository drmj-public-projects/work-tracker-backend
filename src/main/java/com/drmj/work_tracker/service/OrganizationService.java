package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.Organization;

import java.util.List;
import java.util.UUID;

public interface OrganizationService {
    boolean validateIfExists(UUID organizationId);

    Organization getById(UUID organizationId);

    Organization getReference(UUID id);

    Organization createOrganization(UUID userId, String name, String timeZone);

    List<Organization> getAllByIds(List<UUID> ids);
}
