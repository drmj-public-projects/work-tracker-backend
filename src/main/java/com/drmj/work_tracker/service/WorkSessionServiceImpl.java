package com.drmj.work_tracker.service;

import com.drmj.work_tracker.dto.request.workSession.CreateManualWorkSessionRequest;
import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.entity.*;
import com.drmj.work_tracker.entity.enums.WorkSessionEntryType;
import com.drmj.work_tracker.entity.enums.WorkSessionSource;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.HourlyRateRepository;
import com.drmj.work_tracker.repository.WorkSessionRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import com.drmj.work_tracker.utils.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkSessionServiceImpl implements WorkSessionService {
    private final PlaceService placeService;
    private final WorkSessionRepository workSessionRepository;
    private final HourlyRateRepository hourlyRateRepository;
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
        Place place = placeService.getById(request.getPlaceId());

        OffsetDateTime startTime = OffsetDateTime.now(java.time.ZoneOffset.UTC);

        BigDecimal currentRate = hourlyRateRepository.findActiveRate(
                        request.getUserId(),
                        request.getOrganizationId(),
                        request.getPlaceId(),
                        startTime
                )
                .map(HourlyRate::getRate)
                .orElseThrow(() -> new BusinessException(ErrorMessage.HOURLY_RATE_NOT_FOUND_MESSAGE.getMessage()));

        WorkSession session = WorkSession.builder()
                .user(userProxy)
                .userId(userProxy.getId())
                .organization(orgProxy)
                .organizationId(orgProxy.getId())
                .place(place)
                .placeId(place.getId())
                .startTime(startTime)
                .breakMinutes(request.getBreakMinutes())
                .status(WorkSessionStatus.ACTIVE)
                .entryType(WorkSessionEntryType.TIMER)
                .source(WorkSessionSource.WEB)
                .hourlyRate(currentRate)
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .notes(request.getNotes())
                .totalPay(BigDecimal.ZERO)
                .build();
        return workSessionRepository.save(session);
    }

    @Override
    public WorkSession endSession(WorkSession workSession) {
        if (workSession.getEndTime() == null) {
            workSession.setEndTime(OffsetDateTime.now(java.time.ZoneOffset.UTC));
        }
        int durationMinutes = com.drmj.work_tracker.utils.Utils.calculateDurationMinutes(
                workSession.getStartTime(),
                workSession.getEndTime(),
                workSession.getBreakMinutes()
        );
        workSession.setDurationMinutes(durationMinutes);
        workSession.setStatus(WorkSessionStatus.COMPLETED);
        BigDecimal pay = calculateTotalPay(durationMinutes, workSession.getHourlyRate());
        workSession.setTotalPay(pay);
        return workSessionRepository.save(workSession);
    }

    private BigDecimal calculateTotalPay(int durationMinutes, BigDecimal hourlyRate) {
        if (durationMinutes <= 0 || hourlyRate == null) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(durationMinutes)
                .divide(BigDecimal.valueOf(60), 10, RoundingMode.HALF_UP)
                .multiply(hourlyRate)
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean existsOverlappingSession(UUID userId, OffsetDateTime startTime, OffsetDateTime endTime) {
        return workSessionRepository.existsOverlappingSession(userId, startTime, endTime);
    }

    @Override
    public WorkSession createManualSession(CreateManualWorkSessionRequest request, int durationMinutes, double locationAccuracy) {
        User userProxy = entityManager.getReference(User.class, request.getUserId());
        Organization orgProxy = entityManager.getReference(Organization.class, request.getOrganizationId());
        Place placeProxy = entityManager.getReference(Place.class, request.getPlaceId());
        BigDecimal currentRate = hourlyRateRepository.findActiveRate(
                        request.getUserId(),
                        request.getOrganizationId(),
                        request.getPlaceId(),
                        request.getStartTime()
                )
                .map(HourlyRate::getRate)
                .orElseThrow(() -> new BusinessException(ErrorMessage.HOURLY_RATE_NOT_FOUND_MESSAGE.getMessage()));
        WorkSession session = WorkSession.builder()
                .user(userProxy)
                .organization(orgProxy)
                .place(placeProxy)
                .userId(userProxy.getId())
                .organizationId(orgProxy.getId())
                .placeId(placeProxy.getId())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .durationMinutes(durationMinutes)
                .status(WorkSessionStatus.COMPLETED)
                .breakMinutes(request.getBreakMinutes())
                .entryType(WorkSessionEntryType.MANUAL)
                .source(WorkSessionSource.WEB)
                .hourlyRate(currentRate)
                .totalPay(calculateTotalPay(durationMinutes, currentRate))
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .locationAccuracy(locationAccuracy)
                .isEdited(false)
                .notes(request.getNotes())
                .build();
        return workSessionRepository.save(session);
    }

    @Override
    public WorkSession update(WorkSession newEntity) {
        return workSessionRepository.save(newEntity);
    }

    @Override
    public WorkSession updateSessionTimes(WorkSession session, OffsetDateTime startTime, OffsetDateTime endTime, Integer breakMinutes) {
        boolean startTimeChanged = !session.getStartTime().isEqual(startTime);
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        if (breakMinutes != null) {
            session.setBreakMinutes(breakMinutes);
        }
        int duration = Utils.calculateDurationMinutes(
                startTime,
                endTime,
                session.getBreakMinutes());
        session.setDurationMinutes(duration);

        BigDecimal hourlyRate = session.getHourlyRate();
        if (startTimeChanged) {
            hourlyRate = hourlyRateRepository.findActiveRate(
                            session.getUserId(),
                            session.getOrganizationId(),
                            session.getPlaceId(),
                            startTime
                    )
                    .map(HourlyRate::getRate)
                    .orElseThrow(() -> new BusinessException(ErrorMessage.HOURLY_RATE_NOT_FOUND_MESSAGE.getMessage()));
            session.setHourlyRate(hourlyRate);
        }

        session.setTotalPay(calculateTotalPay(duration, hourlyRate));
        session.setIsEdited(true);
        session.setEditedAt(OffsetDateTime.now(java.time.ZoneOffset.UTC));

        return workSessionRepository.save(session);
    }

    @Override
    public boolean existsOverlappingSessionExcludingId(UUID userId, OffsetDateTime startTime, OffsetDateTime endTime, UUID excludedId) {
        return workSessionRepository.existsOverlappingSession(userId, startTime, endTime, excludedId);
    }
}
