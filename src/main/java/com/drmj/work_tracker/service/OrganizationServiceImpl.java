package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.entity.OrganizationSettings;
import com.drmj.work_tracker.entity.User;
import com.drmj.work_tracker.entity.UserOrganization;
import com.drmj.work_tracker.entity.enums.UserOrganizationRole;
import com.drmj.work_tracker.exception.BusinessException;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.OrganizationRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;
    private final UserService userService;
    private final UserOrganizationService userOrganizationService;
    private final OrganizationSettingsService organizationSettingsService;

    private static final long MAX_ORG_TO_CREATE= 1;

    @Override
    public boolean validateIfExists(UUID organizationId) {
        return organizationRepository.existsById(organizationId);
    }

    @Override
    public Organization getById(UUID organizationId) {
        return organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.ORGANIZATION_NOT_FOUND_MESSAGE.getMessage()));
    }

    @Override
    public Organization getReference(UUID id) {
        return organizationRepository.getReferenceById(id);
    }

    @Override
    @Transactional
    public Organization createOrganization(UUID userId, String name, String timeZone) {
        long orgCount = organizationRepository.countByCreatedBy(userId);
        if (orgCount >= MAX_ORG_TO_CREATE) {
            throw new BusinessException(ErrorMessage.USER_ORGANIZATION_LIMIT_REACHED.getMessage());
        }
        Organization organization = Organization.builder()
                .name(name)
                .build();
        Organization savedOrg = organizationRepository.save(organization);

        User user = userService.getReference(userId);
        UserOrganization userOrg = UserOrganization.builder()
                .user(user)
                .userId(userId)
                .organization(savedOrg)
                .organizationId(savedOrg.getId())
                .role(UserOrganizationRole.ADMIN)
                .build();
        userOrganizationService.save(userOrg);
        OrganizationSettings orgSettings = OrganizationSettings.builder()
                .organizationId(savedOrg.getId())
                .allowManualEntries(false)
                .allowEditAfterSubmit(false)
                .requireLocation(false)
                .timeZone(timeZone != null && !timeZone.isBlank() ? timeZone : "UTC")
                .build();
        organizationSettingsService.save(orgSettings);
        return savedOrg;
    }

    @Override
    public List<Organization> getAllByIds(List<UUID> ids) {
        return organizationRepository.findAllById(ids);
    }
}
