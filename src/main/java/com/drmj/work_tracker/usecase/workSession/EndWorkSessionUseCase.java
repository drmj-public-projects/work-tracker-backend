package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.service.WorkSessionService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EndWorkSessionUseCase {
    private final WorkSessionService workSessionService;

    public ApiResponse<WorkSessionResponse> execute(UUID workSessionId, UUID userId) {
        WorkSession session = this.validateWorkSession(workSessionId, userId);
        int durationMinutes = calculateDuration(session);
        WorkSession updated = workSessionService.endSession(session, durationMinutes);
        return new ApiResponse<>(WorkSessionResponse.fromEntity(updated));
    }

    private WorkSession validateWorkSession(UUID workSessionId, UUID userId) {
        WorkSession workSession = workSessionService.getById(workSessionId);
        if (!workSession.getUserId().equals(userId)) {
            throw new BusinessException(ErrorMessage.USER_NOT_OWNER_OF_SESSION.getMessage());
        }
        if (workSession.getStatus() != WorkSessionStatus.ACTIVE) {
            throw new BusinessException(ErrorMessage.SESSION_NOT_ACTIVE.getMessage());
        }
        return workSession;
    }

    private int calculateDuration(WorkSession workSession) {
        OffsetDateTime now = OffsetDateTime.now(java.time.ZoneOffset.UTC);
        workSession.setEndTime(now);
        return (int) java.time.Duration.between(
                workSession.getStartTime(),
                now
        ).toMinutes() - workSession.getBreakMinutes();
    }
}
