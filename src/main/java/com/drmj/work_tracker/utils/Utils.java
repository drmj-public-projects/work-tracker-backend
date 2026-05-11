package com.drmj.work_tracker.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Utils {

    private Utils() {}

    public static int calculateDurationMinutes(OffsetDateTime start, OffsetDateTime end, int breakMinutes) {
        long totalMinutes = Duration.between(start, end).toMinutes();
        int duration = (int) (totalMinutes - breakMinutes);
        return Math.max(duration, 0);
    }

    public static boolean isValidTimeRange(OffsetDateTime start, OffsetDateTime end) {
        if (start == null || end == null) return false;
        return start.isBefore(end);
    }

    public static OffsetDateTime toOffsetDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atOffset(ZoneOffset.UTC);
    }
}
