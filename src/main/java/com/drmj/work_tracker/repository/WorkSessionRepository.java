package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.UUID;

@Repository
public interface WorkSessionRepository extends JpaRepository<WorkSession, UUID> {
    boolean existsByUser_idAndOrganization_idAndStatus(UUID userId,  UUID organizationId, WorkSessionStatus status);

    @Query("""
    SELECT COUNT(ws) > 0
    FROM WorkSession ws
    WHERE ws.userId = :userId
      AND ws.isDeleted = false
      AND (
            ws.startTime < :endTime AND
            (ws.endTime IS NULL OR ws.endTime > :startTime)
          )
    """)
    boolean existsOverlappingSession(UUID userId, OffsetDateTime startTime, OffsetDateTime endTime);

    @Query("""
    SELECT COUNT(ws) > 0
    FROM WorkSession ws
    WHERE ws.userId = :userId
      AND ws.isDeleted = false
      AND ws.id <> :excludeId
      AND (
            ws.startTime < :endTime AND
            (ws.endTime IS NULL OR ws.endTime > :startTime)
          )
    """)
    boolean existsOverlappingSession(
            UUID userId,
            OffsetDateTime startTime,
            OffsetDateTime endTime,
            UUID excludeId
    );
}
