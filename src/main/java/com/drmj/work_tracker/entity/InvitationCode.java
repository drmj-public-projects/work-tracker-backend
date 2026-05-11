package com.drmj.work_tracker.entity;

import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "invitation_codes")
@SQLDelete(sql = "UPDATE invitation_codes SET is_deleted = true, deleted_at = now() WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "organization_id", insertable = false, updatable = false)
    private UUID organizationId;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserOrganizationRole role;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "max_uses", nullable = false)
    @Builder.Default
    private Integer maxUses = 1;

    @Column(name = "current_uses", nullable = false)
    @Builder.Default
    private Integer currentUses = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    public boolean isExpired() {
        return expiresAt != null &&
                expiresAt.isBefore(OffsetDateTime.now());
    }

    public boolean hasAvailableUses() {
        return currentUses < maxUses;
    }

    public boolean canBeUsed() {
        return isActive &&
                !isExpired() &&
                hasAvailableUses();
    }
}