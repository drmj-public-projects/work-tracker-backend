package com.drmj.work_tracker.usecase.organization;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.invitationCode.InvitationCodeStatsResponse;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.InvitationCodeService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetInvitationCodeStatsUseCase {
    private final InvitationCodeService invitationCodeService;
    private final UserOrganizationService userOrganizationService;

    private static final List<UserOrganizationRole> ALLOWED_ROLES = Arrays.asList(
            UserOrganizationRole.EMPLOYER,
            UserOrganizationRole.ADMIN
    );

    public ApiResponse<InvitationCodeStatsResponse> execute(UUID organizationId) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        validateUserAccess(currentUserId, organizationId);
        validateUserRole(currentUserId, organizationId);

        Long activeCodes = invitationCodeService.countActiveByOrganizationId(organizationId);
        Long totalUses = invitationCodeService.sumCurrentUsesByOrganizationId(organizationId);
        Long expiredCodes = invitationCodeService.countExpiredByOrganizationId(organizationId);

        InvitationCodeStatsResponse stats = InvitationCodeStatsResponse.builder()
                .activeCodes(activeCodes != null ? activeCodes : 0L)
                .totalUses(totalUses != null ? totalUses : 0L)
                .expiredCodes(expiredCodes != null ? expiredCodes : 0L)
                .build();

        return new ApiResponse<>(stats);
    }

    private void validateUserAccess(UUID userId, UUID organizationId) {
        boolean hasAccess = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (!hasAccess) {
            throw new BusinessException(ErrorMessage.USER_NOT_IN_ORG.getMessage());
        }
    }

    private void validateUserRole(UUID userId, UUID organizationId) {
        UserOrganization userOrg = userOrganizationService.findByUserIdAndOrganizationId(userId, organizationId);
        if (userOrg == null || !ALLOWED_ROLES.contains(userOrg.getRole())) {
            throw new BusinessException(ErrorMessage.USER_NOT_AUTHORIZED_TO_GENERATE_CODE.getMessage());
        }
    }
}
