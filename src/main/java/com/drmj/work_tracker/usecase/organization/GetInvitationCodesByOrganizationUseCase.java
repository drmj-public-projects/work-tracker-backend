package com.drmj.work_tracker.usecase.organization;

import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.invitationCode.InvitationCodeResponse;
import com.drmj.work_tracker.entity.InvitationCode;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.InvitationCodeService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetInvitationCodesByOrganizationUseCase {
    private final InvitationCodeService invitationCodeService;
    private final UserOrganizationService userOrganizationService;

    private static final List<UserOrganizationRole> ALLOWED_ROLES = Arrays.asList(
            UserOrganizationRole.EMPLOYER,
            UserOrganizationRole.ADMIN
    );

    public ApiResponse<Page<InvitationCodeResponse>> execute(UUID organizationId, int page, int size) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        validateUserAccess(currentUserId, organizationId);
        validateUserRole(currentUserId, organizationId);

        Pageable pageable = PageRequest.of(page, size);
        Page<InvitationCode> codePage = invitationCodeService.findByOrganizationId(organizationId, pageable);
        Page<InvitationCodeResponse> responsePage = codePage.map(InvitationCodeResponse::fromEntity);

        return new ApiResponse<>(responsePage);
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
