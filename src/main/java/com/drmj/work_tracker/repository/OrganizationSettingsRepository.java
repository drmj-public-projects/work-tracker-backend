package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.OrganizationSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrganizationSettingsRepository extends JpaRepository<OrganizationSettings, UUID> {
}
