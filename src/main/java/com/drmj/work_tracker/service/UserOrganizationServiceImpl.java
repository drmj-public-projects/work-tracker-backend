package com.drmj.work_tracker.service;

import com.drmj.work_tracker.repository.UserOrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserOrganizationServiceImpl implements UserOrganizationService {
    private final UserOrganizationRepository userOrganizationRepository;

    @Override
    public boolean existsByUserIdAndOrganizationId(UUID userId, UUID organizationId) {
        return userOrganizationRepository.existsByUser_idAndOrganization_id(userId, organizationId);
    }
}
