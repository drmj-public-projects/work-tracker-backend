package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.request.workSession.WorkSessionDetailQuery;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.service.WorkSessionService;
import com.drmj.work_tracker.utils.ErrorMessage;
import com.drmj.work_tracker.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetWorkSessionDetailUseCase {
    private final WorkSessionService workSessionService;

    public ApiResponse<List<WorkSessionResponse>> execute(WorkSessionDetailQuery query) {
        validate(query);
        List<WorkSession> sessions = workSessionService.findByFilters(
                query.getPlaceId(),
                query.getStartDate(),
                query.getEndDate(),
                query.getStatus(),
                query.getUserId()
        );
        List<WorkSessionResponse> response = sessions.stream()
                .map(WorkSessionResponse::fromEntity)
                .sorted(Comparator.comparing(WorkSessionResponse::getStartTime))
                .toList();
        return new ApiResponse<>(response);
    }

    private void validate(WorkSessionDetailQuery query) {
        if (query.getPlaceId() == null) {
            throw new IllegalArgumentException(ErrorMessage.PLACE_ID_REQUIRED.getMessage());
        }
        if (query.getStartDate() == null || query.getEndDate() == null) {
            throw new IllegalArgumentException(ErrorMessage.START_AND_END_DATE_REQUIRED.getMessage());
        }
        if (!Utils.isValidTimeRange(query.getStartDate(), query.getEndDate())) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_TIME_RANGE.getMessage());
        }
    }
}