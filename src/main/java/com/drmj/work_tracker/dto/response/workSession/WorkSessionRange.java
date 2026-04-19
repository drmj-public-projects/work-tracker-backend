package com.drmj.work_tracker.dto.response.workSession;

import com.drmj.work_tracker.utils.ErrorMessage;

import java.time.*;
import java.time.temporal.ChronoUnit;

public enum WorkSessionRange {
    THIS_WEEK {
        @Override
        public DateRange resolve(OffsetDateTime now, OffsetDateTime start, OffsetDateTime end) {
            return new DateRange(
                    now.with(DayOfWeek.MONDAY).truncatedTo(ChronoUnit.DAYS),
                    now
            );
        }
    },
    LAST_WEEK {
        @Override
        public DateRange resolve(OffsetDateTime now, OffsetDateTime start, OffsetDateTime end) {
            OffsetDateTime startLastWeek = now.minusWeeks(1)
                    .with(DayOfWeek.MONDAY)
                    .truncatedTo(ChronoUnit.DAYS);
            return new DateRange(
                    startLastWeek,
                    startLastWeek.plusDays(6).with(LocalTime.MAX)
            );
        }
    },
    THIS_MONTH {
        @Override
        public DateRange resolve(OffsetDateTime now, OffsetDateTime start, OffsetDateTime end) {
            return new DateRange(
                    now.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS),
                    now
            );
        }
    },

    LAST_MONTH {
        @Override
        public DateRange resolve(OffsetDateTime now, OffsetDateTime start, OffsetDateTime end) {
            OffsetDateTime startLastMonth = now.minusMonths(1)
                    .withDayOfMonth(1)
                    .truncatedTo(ChronoUnit.DAYS);
            return new DateRange(
                    startLastMonth,
                    startLastMonth.withDayOfMonth(startLastMonth.toLocalDate().lengthOfMonth()).with(LocalTime.MAX));
        }
    },
    CUSTOM {
        @Override
        public DateRange resolve(OffsetDateTime now, OffsetDateTime start, OffsetDateTime end) {
            if (start == null || end == null) {
                throw new IllegalArgumentException(ErrorMessage.START_AND_END_DATE_REQUIRED_IN_CUSTOM.getMessage());
            }
            return new DateRange(start, end);
        }
    };

    public abstract DateRange resolve(
            OffsetDateTime now,
            OffsetDateTime start,
            OffsetDateTime end
    );

    public static WorkSessionRange from(String value) {
        try {
            return WorkSessionRange.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_TIME_RANGE.getMessage() + value);
        }
    }
}