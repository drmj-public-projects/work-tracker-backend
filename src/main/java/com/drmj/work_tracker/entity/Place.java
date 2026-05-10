package com.drmj.work_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "places")
@SQLDelete(sql = "UPDATE places SET is_deleted = true, deleted_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "organization_id", insertable = false, updatable = false)
    private UUID organizationId;

    @Column(nullable = false)
    private String name;

    private String description;

    private Double latitude;

    private Double longitude;

    @Column(name = "radius_meters")
    private Integer radiusMeters;

    @Column(name = "is_active")
    private Boolean isActive = true;
}
