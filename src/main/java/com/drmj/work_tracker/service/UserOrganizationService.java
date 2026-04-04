package com.drmj.work_tracker.service;

import java.util.UUID;

public interface UserOrganizationService {
    boolean existsByUserIdAndOrganizationId(UUID userId, UUID organizationId);
}
