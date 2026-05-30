package com.drmj.work_tracker.dto.response.place;

import com.drmj.work_tracker.entity.Place;
import com.drmj.work_tracker.utils.Utils;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDetailResponse {
    private UUID id;
    private String name;
    private String description;
    private Double latitude;
    private Double longitude;
    private Integer radiusMeters;
    private Boolean isActive;
    private BigDecimal hourlyRate;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static PlaceDetailResponse fromPlaceAndRate(Place place, BigDecimal hourlyRate) {
        return PlaceDetailResponse.builder()
                .id(place.getId())
                .name(place.getName())
                .description(place.getDescription())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .radiusMeters(place.getRadiusMeters())
                .isActive(place.getIsActive())
                .hourlyRate(hourlyRate)
                .createdAt(Utils.toOffsetDateTime(place.getCreatedAt()))
                .updatedAt(Utils.toOffsetDateTime(place.getUpdatedAt()))
                .build();
    }
}
