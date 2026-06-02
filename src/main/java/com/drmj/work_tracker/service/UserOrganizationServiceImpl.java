package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.repository.UserOrganizationRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public UserOrganization save(UserOrganization userOrganization) {
        return userOrganizationRepository.save(userOrganization);
    }

    @Override
    public Map<UUID, Long> countMembersByOrganizationIds(List<UUID> organizationIds) {
        if (organizationIds == null || organizationIds.isEmpty()) {
            return new HashMap<>();
        }
        List<Object[]> results = userOrganizationRepository.countMembersByOrganizationIds(organizationIds);
        Map<UUID, Long> counts = new HashMap<>();
        for (Object[] row : results) {
            counts.put((UUID) row[0], (Long) row[1]);
        }
        return counts;
    }

    @Override
    public List<UserOrganization> findByOrganizationId(UUID organizationId) {
        return userOrganizationRepository.findByOrganizationIdAndIsDeletedFalse(organizationId);
    }
}
