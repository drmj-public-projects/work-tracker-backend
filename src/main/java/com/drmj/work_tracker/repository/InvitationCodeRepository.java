package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.InvitationCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationCodeRepository extends JpaRepository<InvitationCode, UUID> {

    boolean existsByCode(String code);

    Optional<InvitationCode> findByCode(String code);

    Page<InvitationCode> findByOrganizationId(UUID organizationId, Pageable pageable);

    @Query("SELECT COUNT(ic) FROM InvitationCode ic WHERE ic.organizationId = :organizationId AND ic.isActive = true AND (ic.expiresAt IS NULL OR ic.expiresAt > CURRENT_TIMESTAMP) AND ic.currentUses < ic.maxUses")
    Long countActiveByOrganizationId(@Param("organizationId") UUID organizationId);

    @Query("SELECT COALESCE(SUM(ic.currentUses), 0) FROM InvitationCode ic WHERE ic.organizationId = :organizationId")
    Long sumCurrentUsesByOrganizationId(@Param("organizationId") UUID organizationId);

    @Query("SELECT COUNT(ic) FROM InvitationCode ic WHERE ic.organizationId = :organizationId AND ic.expiresAt IS NOT NULL AND ic.expiresAt <= CURRENT_TIMESTAMP")
    Long countExpiredByOrganizationId(@Param("organizationId") UUID organizationId);
}
