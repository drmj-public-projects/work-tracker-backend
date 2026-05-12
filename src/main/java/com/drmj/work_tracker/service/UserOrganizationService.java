package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.UserOrganization;

import java.util.List;
import java.util.UUID;

public interface UserOrganizationService {
    boolean existsByUserIdAndOrganizationId(UUID userId, UUID organizationId);

    UserOrganization findByUserIdAndOrganizationId(UUID userId, UUID organizationId);

    List<UserOrganization> findByUserId(UUID userId);

    UserOrganization save(UserOrganization userOrganization);
}
