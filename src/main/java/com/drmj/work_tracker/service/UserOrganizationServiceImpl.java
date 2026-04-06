package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.repository.UserOrganizationRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserOrganizationServiceImpl implements UserOrganizationService {
    private final UserOrganizationRepository userOrganizationRepository;

    @Override
    public boolean existsByUserIdAndOrganizationId(UUID userId, UUID organizationId) {
        return userOrganizationRepository.existsByUser_idAndOrganization_id(userId, organizationId);
    }

    @Override
    public UserOrganization findByUserIdAndOrganizationId(UUID userId, UUID organizationId) {
        return userOrganizationRepository.findByUser_idAndOrganization_id(userId, organizationId)
                .orElseThrow(() -> new BusinessException(ErrorMessage.USER_NOT_ACCESS_TO_ORGANIZATION.getMessage()));
    }

    @Override
    public List<UserOrganization> findByUserId(UUID userId) {
        return userOrganizationRepository.findByUser_id(userId);
    }
}
