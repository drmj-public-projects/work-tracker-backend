package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.request.workSession.UpdateWorkSessionRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.service.WorkSessionService;
import com.drmj.work_tracker.utils.ErrorMessage;
import com.drmj.work_tracker.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateWorkSessionUseCase {
    private final WorkSessionService workSessionService;

    public ApiResponse<WorkSessionResponse> execute(UUID id, UpdateWorkSessionRequest request, UUID currentUserId) {
        WorkSession workSession = validateRequest(id, currentUserId);
        validateTime(request, workSession);
        applyTimeUpdates(workSession, request);
        WorkSession updated = workSessionService.update(workSession);
        return new ApiResponse<>(WorkSessionResponse.fromEntity(updated));
    }

    private WorkSession validateRequest(UUID workSessionId, UUID currentUserId) {
        WorkSession workSession = workSessionService.getById(workSessionId);
        if (!workSession.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorMessage.USER_WITH_NOT_PERMISSION_TO_UPDATE_WORK_SESSION.getMessage());
        }
        if (!WorkSessionStatus.COMPLETED.equals(workSession.getStatus())) {
            throw new BusinessException(ErrorMessage.ONLY_COMPLETED_SESSIONS_CAN_BE_UPDATED.getMessage());
        }
        return workSession;
    }

    private void validateTime(UpdateWorkSessionRequest request, WorkSession workSession) {
        if (!Utils.isValidTimeRange(request.getStartTime(), request.getEndTime())) {
            throw new BusinessException(ErrorMessage.INVALID_TIME_RANGE.getMessage());
        }
        boolean isOverlapping = workSessionService.existsOverlappingSessionExcludingId(
                workSession.getUserId(),
                request.getStartTime(),
                request.getEndTime(),
                workSession.getId()
        );
        if (isOverlapping) {
            throw new BusinessException(ErrorMessage.NEW_RANGE_OVERLAPS.getMessage());
        }
    }

    private void applyTimeUpdates(WorkSession entity, UpdateWorkSessionRequest request) {
        entity.setStartTime(request.getStartTime());
        entity.setEndTime(request.getEndTime());
        if (request.getBreakMinutes() != null) {
            entity.setBreakMinutes(request.getBreakMinutes());
        }
        int duration = Utils.calculateDurationMinutes(
                request.getStartTime(),
                request.getEndTime(),
                entity.getBreakMinutes());
        entity.setDurationMinutes(duration);
        entity.setIsEdited(true);
        entity.setEditedAt(OffsetDateTime.now(java.time.ZoneOffset.UTC));
        entity.setUpdatedAt(OffsetDateTime.now(java.time.ZoneOffset.UTC));
    }
}