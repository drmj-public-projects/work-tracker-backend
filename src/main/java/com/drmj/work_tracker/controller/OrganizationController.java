package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.invitationCode.GenerateInvitationCodeRequest;
import com.drmj.work_tracker.dto.request.organization.CreateOrganizationRequest;
import com.drmj.work_tracker.dto.request.organization.CreateOrganizationSettingsRequest;
import com.drmj.work_tracker.dto.request.organization.SaveOrganizationSettingsRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.invitationCode.InvitationCodeResponse;
import com.drmj.work_tracker.dto.response.invitationCode.InvitationCodeStatsResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationDetailResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationSettingsResponse;
import com.drmj.work_tracker.usecase.organization.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    private final GetOrganizationSettingsUseCase getOrganizationSettingsUseCase;
    private final GetInvitationCodesByOrganizationUseCase getInvitationCodesByOrganizationUseCase;
    private final GetInvitationCodeStatsUseCase getInvitationCodeStatsUseCase;
    private final RevokeInvitationCodeUseCase revokeInvitationCodeUseCase;

    @PostMapping("/invite-code")
    public ApiResponse<InvitationCodeResponse> generate(@RequestBody @Valid GenerateInvitationCodeRequest request) {
        return generateInvitationCodeUseCase.execute(request);
    }

    @GetMapping("/{id}/invitation-codes")
    public ApiResponse<Page<InvitationCodeResponse>> getInvitationCodes(
            @PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return getInvitationCodesByOrganizationUseCase.execute(id, page, size);
    }

    @GetMapping("/{id}/invitation-codes/stats")
    public ApiResponse<InvitationCodeStatsResponse> getInvitationCodeStats(@PathVariable UUID id) {
        return getInvitationCodeStatsUseCase.execute(id);
    }

    @DeleteMapping("/invitation-codes/{invitationCodeId}")
    public ApiResponse<InvitationCodeResponse> revokeInvitationCode(@PathVariable UUID invitationCodeId) {
        return revokeInvitationCodeUseCase.execute(invitationCodeId);
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

    @GetMapping("/{id}/settings")
    public ApiResponse<OrganizationSettingsResponse> getSettings(@PathVariable UUID id) {
        return getOrganizationSettingsUseCase.execute(id);
    }
}
