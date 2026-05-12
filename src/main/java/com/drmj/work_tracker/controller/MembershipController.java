package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.membership.JoinOrganizationRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.membership.MembershipResponse;
import com.drmj.work_tracker.usecase.membership.JoinOrganizationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/membership")
@RequiredArgsConstructor
public class MembershipController {
    private final JoinOrganizationUseCase joinOrganizationUseCase;

    @PostMapping("/join-organization")
    public ApiResponse<MembershipResponse> joinOrganization(
            @RequestBody @Valid JoinOrganizationRequest request
    ) {
        return joinOrganizationUseCase.execute(request);
    }
}
