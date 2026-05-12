package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.InvitationCode;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.InvitationCodeRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

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

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
