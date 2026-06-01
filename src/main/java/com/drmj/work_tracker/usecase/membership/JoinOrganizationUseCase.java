package com.drmj.work_tracker.usecase.membership;

import com.drmj.work_tracker.dto.request.membership.JoinOrganizationRequest;
import com.drmj.work_tracker.dto.response.ApiResponse;
import com.drmj.work_tracker.dto.response.membership.MembershipResponse;
import com.drmj.work_tracker.entity.InvitationCode;
import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.security.SecurityUtils;
import com.drmj.work_tracker.service.InvitationCodeService;
import com.drmj.work_tracker.service.OrganizationService;
import com.drmj.work_tracker.service.UserOrganizationService;
import com.drmj.work_tracker.service.UserService;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JoinOrganizationUseCase {
    private final InvitationCodeService invitationCodeService;
    private final UserOrganizationService userOrganizationService;
    private final UserService userService;
    private final OrganizationService organizationService;

    @Transactional
    public ApiResponse<MembershipResponse> execute(JoinOrganizationRequest request) {
        UUID currentUserId = SecurityUtils.getCurrentUserId();
        String normalizedCode = request.getInvitationCode().trim().toUpperCase();

        InvitationCode invitationCode = invitationCodeService.findByCode(normalizedCode);

        validateInvitationCode(invitationCode);
        updateRoles(invitationCode);
        validateUserNotAlreadyMember(currentUserId, invitationCode.getOrganizationId());

        UserOrganization membership = createMembership(currentUserId, invitationCode);

        updateInvitationCodeUsage(invitationCode);

        return new ApiResponse<>(MembershipResponse.fromEntity(membership));
    }
    private void validateInvitationCode(InvitationCode invitationCode) {
        if (invitationCode.getIsActive() == null || !invitationCode.getIsActive()) {
            throw new BusinessException(ErrorMessage.INVALID_INVITATION_CODE.getMessage());
        }
        if (invitationCode.isExpired()) {
            throw new BusinessException(ErrorMessage.INVITATION_CODE_EXPIRED.getMessage());
        }
        if (!invitationCode.hasAvailableUses()) {
            throw new BusinessException(ErrorMessage.INVITATION_CODE_MAX_USES_REACHED.getMessage());
        }
    }

    private void validateUserNotAlreadyMember(UUID userId, UUID organizationId) {
        boolean alreadyMember = userOrganizationService.existsByUserIdAndOrganizationId(userId, organizationId);
        if (alreadyMember) {
            throw new BusinessException(ErrorMessage.USER_ALREADY_MEMBER.getMessage());
        }
    }

    private UserOrganization createMembership(UUID userId, InvitationCode invitationCode) {
        User user = userService.getReference(userId);
        Organization organization = organizationService.getReference(invitationCode.getOrganizationId());
        UserOrganization membership = UserOrganization.builder()
                .user(user)
                .userId(userId)
                .organization(organization)
                .organizationId(organization.getId())
                .role(invitationCode.getRole())
                .build();
        return userOrganizationService.save(membership);
    }

    private void updateInvitationCodeUsage(InvitationCode invitationCode) {
        invitationCode.setCurrentUses(invitationCode.getCurrentUses() + 1);
        if (!invitationCode.hasAvailableUses()) {
            invitationCode.setIsActive(false);
        }
        invitationCodeService.save(invitationCode);
    }

    private void updateRoles(InvitationCode invitationCode) {
        if (UserOrganizationRole.ADMIN.equals(invitationCode.getRole())) {
            invitationCode.setRole(UserOrganizationRole.EMPLOYER);
            return;
        }
        if (UserOrganizationRole.EMPLOYER.equals(invitationCode.getRole())) {
            invitationCode.setRole(UserOrganizationRole.EMPLOYEE);
            return;
        }
        throw new BusinessException(ErrorMessage.ONLY_ADMINS_AND_EMPLOYERS_CAN_JOIN_MEMBERS.getMessage());
    }
}