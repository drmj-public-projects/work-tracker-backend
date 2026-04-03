package com.drmj.work_tracker.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "organization_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationSettings {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false, unique = true)
    private Organization organization;

    @Column(name = "require_location")
    private Boolean requireLocation;

    @Column(name = "allow_manual_entries")
    private Boolean allowManualEntries;

    @Column(name = "allow_edit_after_submit")
    private Boolean allowEditAfterSubmit;
}