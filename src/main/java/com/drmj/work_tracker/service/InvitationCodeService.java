package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.InvitationCode;

public interface InvitationCodeService {
    InvitationCode save(InvitationCode invitationCode);

    boolean existsByCode(String code);

    String generateUniqueCode();

    InvitationCode findByCode(String code);
}