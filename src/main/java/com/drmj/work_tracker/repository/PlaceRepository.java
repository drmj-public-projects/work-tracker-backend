package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaceRepository extends JpaRepository<Place, UUID> {
    List<Place> findByOrganization_id(UUID organizationId);
}
