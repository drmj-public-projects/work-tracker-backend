package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.HourlyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HourlyRateRepository extends JpaRepository<HourlyRate, UUID> {
    @Query("""
    SELECT hr
    FROM HourlyRate hr
    WHERE hr.userId = :userId
      AND hr.organizationId = :organizationId
      AND hr.placeId = :placeId
      AND hr.validFrom <= :time
      AND (hr.validTo IS NULL OR hr.validTo >= :time)
      AND hr.isDeleted = false
    """)
    Optional<HourlyRate> findActiveRate(UUID userId, UUID organizationId, UUID placeId, OffsetDateTime time);
}
