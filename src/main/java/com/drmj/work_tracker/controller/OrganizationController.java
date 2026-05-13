package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.invitationCode.GenerateInvitationCodeRequest;
import com.drmj.work_tracker.dto.request.organization.CreateOrganizationRequest;
import com.drmj.work_tracker.dto.request.organization.CreateOrganizationSettingsRequest;
import com.drmj.work_tracker.dto.request.organization.SaveOrganizationSettingsRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.invitationCode.InvitationCodeResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationResponse;
import com.drmj.work_tracker.dto.response.organization.OrganizationSettingsResponse;
import com.drmj.work_tracker.usecase.organization.CreateOrganizationSettingsUseCase;
import com.drmj.work_tracker.usecase.organization.CreateOrganizationUseCase;
import com.drmj.work_tracker.usecase.organization.GenerateInvitationCodeUseCase;
import com.drmj.work_tracker.usecase.organization.SaveOrganizationSettingsUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final GenerateInvitationCodeUseCase generateInvitationCodeUseCase;
    private final CreateOrganizationUseCase createOrganizationUseCase;
    private final SaveOrganizationSettingsUseCase saveOrganizationSettingsUseCase;
    private final CreateOrganizationSettingsUseCase createOrganizationSettingsUseCase;

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
}
