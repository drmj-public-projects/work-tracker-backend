package com.drmj.work_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(
        name = "organization_settings",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_org_settings", columnNames = "organization_id")
        }
)
@SQLDelete(sql = "UPDATE places SET is_deleted = true, deleted_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationSettings extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "require_location", nullable = false)
    private Boolean requireLocation = false;

    @Column(name = "allow_manual_entries", nullable = false)
    private Boolean allowManualEntries = true;

    @Column(name = "allow_edit_after_submit", nullable = false)
    private Boolean allowEditAfterSubmit = true;
}