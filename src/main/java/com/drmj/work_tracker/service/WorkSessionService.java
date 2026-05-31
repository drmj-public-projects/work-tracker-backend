package com.drmj.work_tracker.service;

import com.drmj.work_tracker.dto.request.workSession.CreateManualWorkSessionRequest;
import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface WorkSessionService {
    WorkSession getById(UUID id);

    WorkSession startSession(StartWorkSessionRequest request);

    WorkSession endSession(WorkSession workSession);

    boolean existsOverlappingSession(UUID userId, OffsetDateTime startTime, OffsetDateTime endTime);

    WorkSession createManualSession(CreateManualWorkSessionRequest request, int breakMinutes, double locationAccuracy);

    WorkSession update(WorkSession newEntity);

    WorkSession updateSessionTimes(WorkSession session, OffsetDateTime startTime, OffsetDateTime endTime, Integer breakMinutes);

    boolean existsOverlappingSessionExcludingId(UUID userId, OffsetDateTime startTime, OffsetDateTime endTime, UUID excludedId);

    List<WorkSession> findByFilters(
            UUID placeId,
            UUID organizationId,
            OffsetDateTime start,
            OffsetDateTime end,
            List<WorkSessionStatus> status,
            UUID userId
    );

    Page<WorkSession> findByFiltersPaginated(
            UUID placeId,
            UUID organizationId,
            OffsetDateTime start,
            OffsetDateTime end,
            List<WorkSessionStatus> status,
            UUID userId,
            Pageable pageable
    );

    java.util.Optional<WorkSession> findActiveSessionByUserId(UUID userId);

    WorkSession updateActiveSession(WorkSession session, String notes, Integer breakMinutes);
}
