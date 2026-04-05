package com.drmj.work_tracker.utils;

import java.time.Duration;
import java.time.OffsetDateTime;

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
}
