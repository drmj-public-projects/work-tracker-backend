package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
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

    @Query("""
    SELECT ws FROM WorkSession ws
    WHERE ws.userId = :userId
      AND ws.startTime BETWEEN :start AND :end
      AND (:placeId IS NULL OR ws.placeId = :placeId)
      AND (:organizationId IS NULL OR ws.organizationId = :organizationId)
      AND (:status IS NULL OR ws.status IN :status)
    """)
    List<WorkSession> findByFilters(
            UUID placeId,
            UUID organizationId,
            OffsetDateTime start,
            OffsetDateTime end,
            List<WorkSessionStatus> status,
            UUID userId
    );

    @Query("""
    SELECT ws FROM WorkSession ws
    WHERE ws.userId = :userId
      AND ws.startTime BETWEEN :start AND :end
      AND (:placeId IS NULL OR ws.placeId = :placeId)
      AND (:organizationId IS NULL OR ws.organizationId = :organizationId)
      AND (:status IS NULL OR ws.status IN :status)
    """)
    Page<WorkSession> findByFiltersPaginated(
            UUID placeId,
            UUID organizationId,
            OffsetDateTime start,
            OffsetDateTime end,
            List<WorkSessionStatus> status,
            UUID userId,
            Pageable pageable
    );

    @Query("""
    SELECT ws FROM WorkSession ws
    WHERE ws.userId = :userId
      AND ws.status = :status
      AND ws.isDeleted = false
    ORDER BY ws.createdAt DESC
    """)
    Optional<WorkSession> findFirstByUserIdAndStatus(UUID userId, WorkSessionStatus status);
}
