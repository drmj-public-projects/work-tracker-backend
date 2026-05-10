package com.drmj.work_tracker.dto.request.place;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreatePlaceRequest {
    @NotNull
    private UUID organizationId;
    
    @NotBlank
    private String name;
    
    private String description;
    
    private Double latitude;
    
    private Double longitude;
    
    private Integer radiusMeters;
}