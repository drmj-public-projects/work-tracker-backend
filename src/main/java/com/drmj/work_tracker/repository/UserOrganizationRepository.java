package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.UserOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserOrganizationRepository extends JpaRepository<UserOrganization, UUID> {
    boolean existsByUser_idAndOrganization_id(UUID userId, UUID organizationId);

    Optional<UserOrganization> findByUser_idAndOrganization_id(UUID userId, UUID organizationId);

    List<UserOrganization> findByUser_id(UUID userId);

    @Query("""
    SELECT uo.organizationId, COUNT(uo)
    FROM UserOrganization uo
    WHERE uo.organizationId IN :organizationIds
      AND uo.isDeleted = false
    GROUP BY uo.organizationId
    """)
    List<Object[]> countMembersByOrganizationIds(List<UUID> organizationIds);
}
