package com.drmj.work_tracker.service;

import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionEntryType;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.repository.WorkSessionRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class WorkSessionServiceImpl implements WorkSessionService {
    private final WorkSessionRepository workSessionRepository;
    @PersistenceContext
    private final EntityManager entityManager;

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
}
