package com.drmj.work_tracker.dto.request.organization;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrganizationRequest {
    @NotBlank(message = "Organization name is required")
    private String name;
}
