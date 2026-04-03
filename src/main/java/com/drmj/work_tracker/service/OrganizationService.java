package com.drmj.work_tracker.service;

import java.util.UUID;

public interface OrganizationService {
    boolean validateIfExists(UUID organizationId);
}
