package com.drmj.work_tracker.service;

import com.drmj.work_tracker.dto.request.workSession.CreateManualWorkSessionRequest;
import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionEntryType;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.WorkSessionRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkSessionServiceImpl implements WorkSessionService {
    private final WorkSessionRepository workSessionRepository;
    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public WorkSession getById(UUID id) {
        return workSessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.WORK_SESSION_NOT_FOUND_MESSAGE.getMessage()));
    }

    @Override
    public WorkSession startSession(StartWorkSessionRequest request) {
        boolean hasActiveSession = workSessionRepository
                .existsByUser_idAndOrganization_idAndStatus(
                        request.getUserId(),
                        request.getOrganizationId(),
                        WorkSessionStatus.ACTIVE
                );
        if (hasActiveSession) {
            throw new BusinessException(ErrorMessage.USER_HAS_ACTIVE_SESSION.getMessage());
        }
        User userProxy = entityManager.getReference(User.class, request.getUserId());
        Organization orgProxy = entityManager.getReference(Organization.class, request.getOrganizationId());
        Place placeProxy = entityManager.getReference(Place.class, request.getPlaceId());
        WorkSession session = WorkSession.builder()
                .user(userProxy)
                .organization(orgProxy)
                .place(placeProxy)
                .startTime(OffsetDateTime.now(java.time.ZoneOffset.UTC))
                .status(WorkSessionStatus.ACTIVE)
                .entryType(WorkSessionEntryType.TIMER)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .notes(request.getNotes())
                .build();
        return workSessionRepository.save(session);
    }

    @Override
    public WorkSession endSession(WorkSession workSession) {
        OffsetDateTime now = OffsetDateTime.now(java.time.ZoneOffset.UTC);
        workSession.setEndTime(now);
        long minutes = java.time.Duration.between(
                workSession.getStartTime(),
                now
        ).toMinutes();
        workSession.setDurationMinutes((int) minutes);
        workSession.setStatus(WorkSessionStatus.COMPLETED);
        return workSessionRepository.save(workSession);
    }

    @Override
    public boolean existsOverlappingSession(UUID userId, OffsetDateTime startTime, OffsetDateTime endTime) {
        return workSessionRepository.existsOverlappingSession(userId, startTime, endTime);
    }

    @Override
    public WorkSession createManualSession(CreateManualWorkSessionRequest request) {
        User userProxy = entityManager.getReference(User.class, request.getUserId());
        Organization orgProxy = entityManager.getReference(Organization.class, request.getOrganizationId());
        Place placeProxy = entityManager.getReference(Place.class, request.getPlaceId());
        long durationMinutes = java.time.Duration.between(
                request.getStartTime(),
                request.getEndTime()
        ).toMinutes();
        WorkSession session = WorkSession.builder()
                .user(userProxy)
                .organization(orgProxy)
                .place(placeProxy)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .durationMinutes((int) durationMinutes)
                .status(WorkSessionStatus.COMPLETED)
                .entryType(WorkSessionEntryType.MANUAL)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .notes(request.getNotes())
                .build();
        return workSessionRepository.save(session);
    }
}
