package com.drmj.work_tracker.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private UUID organizationId;
    private String role;
}
