package com.drmj.work_tracker.dto.request.membership;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinOrganizationRequest {
    @NotBlank
    private String invitationCode;
}
