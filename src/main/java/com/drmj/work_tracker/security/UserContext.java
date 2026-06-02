package com.drmj.work_tracker.security;

import java.util.UUID;

public record UserContext(
        UUID userId,
        UUID organizationId,
        String role
) {
}