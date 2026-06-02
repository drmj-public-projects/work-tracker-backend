package com.drmj.work_tracker.dto.request.place;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePlaceRequest {
    @NotBlank(message = "Place name is required")
    private String name;
    
    private String description;
    
    private Double latitude;
    
    private Double longitude;
    
    private Integer radiusMeters;
}
