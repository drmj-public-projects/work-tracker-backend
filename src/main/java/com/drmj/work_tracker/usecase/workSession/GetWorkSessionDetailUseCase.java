package com.drmj.work_tracker.usecase.workSession;

import com.drmj.work_tracker.dto.request.workSession.WorkSessionDetailQuery;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionResponse;
import com.drmj.work_tracker.dto.response.workSession.WorkSessionRange;
import com.drmj.work_tracker.dto.response.workSession.DateRange;
import com.drmj.work_tracker.entity.WorkSession;
import com.drmj.work_tracker.service.WorkSessionService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class GetWorkSessionDetailUseCase {
    private final WorkSessionService workSessionService;

    public ApiResponse<Page<WorkSessionResponse>> execute(WorkSessionDetailQuery query) {
        validate(query);

        DateRange range;
        if (query.getRange() != null && !query.getRange().isBlank()) {
            WorkSessionRange rangeEnum = WorkSessionRange.from(query.getRange());
            range = rangeEnum.resolve(
                    OffsetDateTime.now(),
                    query.getStartDate(),
                    query.getEndDate()
            );
        } else {
            range = new DateRange(query.getStartDate(), query.getEndDate());
        }

        int pageNumber = query.getPage() != null ? query.getPage() : 0;
        int pageSize = query.getSize() != null ? query.getSize() : 20;
        if (pageSize > 100) {
            pageSize = 100;
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<WorkSession> sessionPage = workSessionService.findByFiltersPaginated(
                query.getPlaceId(),
                query.getOrganizationId(),
                range.getStart(),
                range.getEnd(),
                query.getStatus(),
                query.getUserId(),
                pageable
        );

        Page<WorkSessionResponse> responsePage = sessionPage.map(WorkSessionResponse::fromEntity);

        return new ApiResponse<>(responsePage);
    }

    private void validate(WorkSessionDetailQuery query) {
        if (query.getPlaceId() == null && query.getOrganizationId() == null) {
            throw new IllegalArgumentException(ErrorMessage.PLACE_ID_OR_ORGANIZATION_ID_REQUIRED.getMessage());
        }
        if (query.getPlaceId() != null && query.getOrganizationId() != null) {
            throw new IllegalArgumentException(ErrorMessage.ONLY_ONE_OF_PLACE_OR_ORGANIZATION_ALLOWED.getMessage());
        }
        if (query.getRange() == null || query.getRange().isBlank()) {
            if (query.getStartDate() == null || query.getEndDate() == null) {
                throw new IllegalArgumentException(ErrorMessage.START_AND_END_DATE_REQUIRED.getMessage());
            }
        }
    }
}
