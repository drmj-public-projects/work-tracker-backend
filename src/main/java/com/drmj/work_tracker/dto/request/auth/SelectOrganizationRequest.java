package com.drmj.work_tracker.dto.request.auth;

import lombok.Getter;

import java.util.UUID;

@Getter
public class SelectOrganizationRequest {
    private UUID organizationId;
}
