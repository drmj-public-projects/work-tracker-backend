package com.drmj.work_tracker.controller;

import com.drmj.work_tracker.dto.request.membership.JoinOrganizationRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.membership.MembershipResponse;
import com.drmj.work_tracker.dto.response.membership.UserOrganizationResponse;
import com.drmj.work_tracker.usecase.membership.GetUserOrganizationsUseCase;
import com.drmj.work_tracker.usecase.membership.JoinOrganizationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/membership")
@RequiredArgsConstructor
public class MembershipController {
    private final JoinOrganizationUseCase joinOrganizationUseCase;
    private final GetUserOrganizationsUseCase getUserOrganizationsUseCase;

    @PostMapping("/join-organization")
    public ApiResponse<MembershipResponse> joinOrganization(
            @RequestBody @Valid JoinOrganizationRequest request
    ) {
        return joinOrganizationUseCase.execute(request);
    }

    @GetMapping("/my-organizations")
    public ApiResponse<List<UserOrganizationResponse>> getMyOrganizations() {
        return getUserOrganizationsUseCase.execute();
    }
}
