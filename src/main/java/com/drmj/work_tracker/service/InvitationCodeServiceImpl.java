package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.InvitationCode;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.InvitationCodeRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationCodeServiceImpl implements InvitationCodeService {
    private final InvitationCodeRepository invitationCodeRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 12;
    private static final int MAX_ATTEMPTS = 10;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public InvitationCode save(InvitationCode invitationCode) {
        return invitationCodeRepository.save(invitationCode);
    }

    @Override
    public boolean existsByCode(String code) {
        return invitationCodeRepository.existsByCode(code);
    }

    @Override
    public String generateUniqueCode() {
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String code = generateRandomCode();
            if (!existsByCode(code)) {
                return code;
            }
        }
        throw new RuntimeException("Failed to generate unique invitation code after " + MAX_ATTEMPTS + " attempts");
    }

    @Override
    public InvitationCode findByCode(String code) {
        return invitationCodeRepository.findByCode(code)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.INVALID_INVITATION_CODE.getMessage()));
    }

    @Override
    public InvitationCode findById(UUID id) {
        return invitationCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.INVALID_INVITATION_CODE.getMessage()));
    }

    @Override
    public Page<InvitationCode> findByOrganizationId(UUID organizationId, Pageable pageable) {
        return invitationCodeRepository.findByOrganizationId(organizationId, pageable);
    }

    @Override
    public Long countActiveByOrganizationId(UUID organizationId) {
        return invitationCodeRepository.countActiveByOrganizationId(organizationId);
    }

    @Override
    public Long sumCurrentUsesByOrganizationId(UUID organizationId) {
        return invitationCodeRepository.sumCurrentUsesByOrganizationId(organizationId);
    }

    @Override
    public Long countExpiredByOrganizationId(UUID organizationId) {
        return invitationCodeRepository.countExpiredByOrganizationId(organizationId);
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
