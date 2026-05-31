package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.request.workSession.UpdateActiveWorkSessionRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.WorkSessionService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateActiveWorkSessionUseCase {
    private final WorkSessionService workSessionService;

    public ApiResponse<WorkSessionResponse> execute(UpdateActiveWorkSessionRequest request) {
        WorkSession workSession = validateRequest(request.getWorkSessionId(), SecurityUtils.getCurrentUserId());
        WorkSession updated = workSessionService.updateActiveSession(workSession, request.getNotes(), request.getBreakMinutes());
        return new ApiResponse<>(WorkSessionResponse.fromEntity(updated));
    }

    private WorkSession validateRequest(UUID workSessionId, UUID currentUserId) {
        WorkSession workSession = workSessionService.getById(workSessionId);
        if (!workSession.getUserId().equals(currentUserId)) {
            throw new BusinessException(ErrorMessage.USER_WITH_NOT_PERMISSION_TO_UPDATE_WORK_SESSION.getMessage());
        }
        if (!WorkSessionStatus.ACTIVE.equals(workSession.getStatus())) {
            throw new BusinessException(ErrorMessage.SESSION_NOT_ACTIVE.getMessage());
        }
        return workSession;
    }
}
