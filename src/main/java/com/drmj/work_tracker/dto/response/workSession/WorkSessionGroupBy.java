package com.drmj.work_tracker.dto.response.workSession;

import com.drmj.work_tracker.utils.ErrorMessage;

import java.time.OffsetDateTime;
import java.time.temporal.WeekFields;

public enum WorkSessionGroupBy {
    DAY {
        @Override
        public String getKey(OffsetDateTime date) {
            return date.toLocalDate().toString();
        }
    },
    WEEK {
        @Override
        public String getKey(OffsetDateTime date) {
            WeekFields wf = WeekFields.ISO;
            return date.getYear() + "-W" + date.get(wf.weekOfWeekBasedYear());
        }
    },
    MONTH {
        @Override
        public String getKey(OffsetDateTime date) {
            return date.getYear() + "-" + date.getMonthValue();
        }
    };
    public abstract String getKey(OffsetDateTime date);

    public static WorkSessionGroupBy from(String value) {
        if (value == null) return DAY;
        try {
            return WorkSessionGroupBy.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_GROUP_BY.getMessage() + value);
        }
    }
}
