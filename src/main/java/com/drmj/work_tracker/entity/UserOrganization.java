package com.drmj.work_tracker.entity;

import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "user_organizations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "organization_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrganization {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserOrganizationRole role;
}
