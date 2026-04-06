package com.drmj.work_tracker.dto.response.auth;

import com.drmj.work_tracker.dto.response.organization.OrganizationResponse;
import com.drmj.work_tracker.entity.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UUID userId;
    private String name;
    private String email;
    List<OrganizationResponse> organizationList;
}
