package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.InvitationCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface InvitationCodeService {
    InvitationCode save(InvitationCode invitationCode);

    boolean existsByCode(String code);

    String generateUniqueCode();

    InvitationCode findByCode(String code);

    InvitationCode findById(UUID id);

    Page<InvitationCode> findByOrganizationId(UUID organizationId, Pageable pageable);

    Long countActiveByOrganizationId(UUID organizationId);

    Long sumCurrentUsesByOrganizationId(UUID organizationId);

    Long countExpiredByOrganizationId(UUID organizationId);
}