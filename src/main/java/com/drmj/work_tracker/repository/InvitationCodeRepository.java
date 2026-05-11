package com.drmj.work_tracker.repository;

import com.drmj.work_tracker.entity.InvitationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InvitationCodeRepository extends JpaRepository<InvitationCode, UUID> {

    boolean existsByCode(String code);
}
