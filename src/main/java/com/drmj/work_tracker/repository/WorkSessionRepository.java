package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkSessionRepository extends JpaRepository<WorkSession, UUID> {
    boolean existsByUser_idAndOrganization_idAndStatus(UUID userId,  UUID organizationId, WorkSessionStatus status);
}
