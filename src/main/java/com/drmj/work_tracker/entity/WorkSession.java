package com.drmj.work_tracker.entity;

import com.drmj.work_tracker.entity.enums.WorkSessionEntryType;
import com.drmj.work_tracker.entity.enums.WorkSessionSource;
import com.drmj.work_tracker.entity.enums.WorkSessionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "work_sessions")
@SQLDelete(sql = "UPDATE places SET is_deleted = true, deleted_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSession extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @Column(name = "start_time", nullable = false)
    private java.time.OffsetDateTime startTime;

    @Column(name = "end_time")
    private java.time.OffsetDateTime endTime;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "break_minutes")
    private Integer breakMinutes;

    private String notes;

    @Enumerated(EnumType.STRING)
    private WorkSessionStatus status;
    @Enumerated(EnumType.STRING)
    private WorkSessionEntryType entryType;
    @Enumerated(EnumType.STRING)
    private WorkSessionSource source;

    private Double latitude;
    private Double longitude;

    @Column(name = "location_accuracy")
    private Double locationAccuracy;

    @Column(name = "is_edited")
    private Boolean isEdited;

    @Column(name = "edited_at")
    private java.time.OffsetDateTime editedAt;
}
