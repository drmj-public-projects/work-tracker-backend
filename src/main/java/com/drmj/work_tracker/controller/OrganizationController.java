package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.invitationCode.GenerateInvitationCodeRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.invitationCode.InvitationCodeResponse;
import com.drmj.work_tracker.usecase.organization.GenerateInvitationCodeUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
public class OrganizationController {
    private final GenerateInvitationCodeUseCase generateInvitationCodeUseCase;

    @PostMapping("/invite-code")
    public ApiResponse<InvitationCodeResponse> generate(
            @RequestBody @Valid GenerateInvitationCodeRequest request
    ) {
        return generateInvitationCodeUseCase.execute(request);
    }
}
