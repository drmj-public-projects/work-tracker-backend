package com.drmj.work_tracker.entity;

import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Table(name = "user_organizations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "organization_id"}))
@SQLDelete(sql = "UPDATE places SET is_deleted = true, deleted_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
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
