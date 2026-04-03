package com.drmj.work_tracker.dto.response.place;

import com.drmj.work_tracker.entity.Place;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceResponse {
    private UUID id;
    private String name;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static PlaceResponse buildFromPlace(Place place) {
        return PlaceResponse.builder()
                .id(place.getId())
                .name(place.getName())
                .description(place.getDescription())
                .createdAt(place.getCreatedAt())
                .updatedAt(place.getUpdatedAt())
                .build();
    }
}
