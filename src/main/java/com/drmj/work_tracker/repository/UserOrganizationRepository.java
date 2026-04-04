package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, UUID> {
    boolean existsByUser_idAndOrganization_id(UUID userId, UUID organizationId);
}
