package com.drmj.work_tracker.usecase.organization;

import com.drmj.work_tracker.dto.request.invitationCode.GenerateInvitationCodeRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.invitationCode.InvitationCodeResponse;
import com.drmj.work_tracker.entity.InvitationCode;
import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.InvitationCodeService;
import com.drmj.work_tracker.service.OrganizationService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenerateInvitationCodeUseCase {
    private final InvitationCodeService invitationCodeService;
    private final OrganizationService organizationService;
    private final UserOrganizationService userOrganizationService;

    private static final List<UserOrganizationRole> ALLOWED_ROLES = Arrays.asList(
            UserOrganizationRole.EMPLOYER,
            UserOrganizationRole.ADMIN
    );

    public ApiResponse<InvitationCodeResponse> execute(GenerateInvitationCodeRequest request) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        validateUserAccess(currentUserId, request.getOrganizationId());
        UserOrganizationRole userRole = validateUserRole(currentUserId, request.getOrganizationId());

        InvitationCode invitationCode = buildInvitationCodeEntity(request, userRole);
        InvitationCode saved = invitationCodeService.save(invitationCode);

        saved.setOrganizationId(request.getOrganizationId());
        return new ApiResponse<>(InvitationCodeResponse.fromEntity(saved));
    }

    private void validateUserAccess(UUID userId, UUID organizationId) {
        boolean hasAccess = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!hasAccess) {
            throw new BusinessException(ErrorMessage.USER_NOT_IN_ORG.getMessage());
        }
    }

    private UserOrganizationRole validateUserRole(UUID userId, UUID organizationId) {
        UserOrganization userOrg = userOrganizationService.findByUserIdAndOrganizationId(userId, organizationId);
        if (userOrg == null || !ALLOWED_ROLES.contains(userOrg.getRole())) {
            throw new BusinessException(ErrorMessage.USER_NOT_AUTHORIZED_TO_GENERATE_CODE.getMessage());
        }
        return userOrg.getRole();
    }

    private InvitationCode buildInvitationCodeEntity(GenerateInvitationCodeRequest request, UserOrganizationRole userRole) {
        Organization organization = organizationService.getById(request.getOrganizationId());
        String uniqueCode = invitationCodeService.generateUniqueCode();

        return InvitationCode.builder()
                .organization(organization)
                .code(uniqueCode)
                .role(userRole)
                .expiresAt(request.getExpiresAt())
                .maxUses(request.getMaxUses() != null ? request.getMaxUses() : 1)
                .currentUses(0)
                .isActive(true)
                .build();
    }
}