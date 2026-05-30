package com.drmj.work_tracker.dto.response.auth;

import com.drmj.work_tracker.dto.response.organization.OrganizationResponse;
import com.drmj.work_tracker.dto.response.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserResponse user;
    List<OrganizationResponse> organizationList;
}
