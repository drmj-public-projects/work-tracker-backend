package com.drmj.work_tracker.dto.response.workSession;

import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class DateRange {
    private final OffsetDateTime start;
    private final OffsetDateTime end;

    public DateRange(OffsetDateTime start, OffsetDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("start y end no pueden ser null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("start no puede ser mayor que end");
        }
        this.start = start;
        this.end = end;
    }
}
