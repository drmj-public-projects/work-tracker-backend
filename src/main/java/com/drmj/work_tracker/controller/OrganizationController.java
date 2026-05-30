package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.invitationCode.GenerateInvitationCodeRequest;
import com.drmj.work_tracker.dto.request.organization.CreateOrganizationRequest;
import com.drmj.work_tracker.dto.request.organization.CreateOrganizationSettingsRequest;
import com.drmj.work_tracker.dto.request.organization.SaveOrganizationSettingsRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.invitationCode.InvitationCodeResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationDetailResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationSettingsResponse;
import com.drmj.work_tracker.usecase.organization.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final GenerateInvitationCodeUseCase generateInvitationCodeUseCase;
    private final CreateOrganizationUseCase createOrganizationUseCase;
    private final SaveOrganizationSettingsUseCase saveOrganizationSettingsUseCase;
    private final CreateOrganizationSettingsUseCase createOrganizationSettingsUseCase;
    private final GetOrganizationDetailUseCase getOrganizationDetailUseCase;
    private final GetOrganizationsByUserUseCase getOrganizationsByUserUseCase;

    @PostMapping("/invite-code")
    public ApiResponse<InvitationCodeResponse> generate(@RequestBody @Valid GenerateInvitationCodeRequest request) {
        return generateInvitationCodeUseCase.execute(request);
    }

    @PostMapping
    public ApiResponse<OrganizationResponse> create(@RequestBody @Valid CreateOrganizationRequest request) {
        return createOrganizationUseCase.execute(request);
    }

    @PutMapping("/{id}/settings")
    public ApiResponse<OrganizationSettingsResponse> saveSettings(
            @PathVariable UUID id,
            @RequestBody @Valid SaveOrganizationSettingsRequest request) {
        return saveOrganizationSettingsUseCase.execute(id, request);
    }

    @PostMapping("/settings")
    public ApiResponse<OrganizationSettingsResponse> createSettings(
            @RequestBody @Valid CreateOrganizationSettingsRequest request) {
        return createOrganizationSettingsUseCase.execute(request);
    }

    @GetMapping("/{id}")
    public ApiResponse<OrganizationDetailResponse> getOrganizationDetail(@PathVariable UUID id) {
        return getOrganizationDetailUseCase.execute(id);
    }

    @GetMapping("/getOrganizations")
    public ApiResponse<List<OrganizationDetailResponse>> getOrganizations(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        return getOrganizationsByUserUseCase.execute(userId);
    }
}
