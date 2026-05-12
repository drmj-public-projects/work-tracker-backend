package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.Organization;
import com.drmj.work_tracker.exception.NotFoundException;
import com.drmj.work_tracker.repository.OrganizationRepository;
import com.drmj.work_tracker.utils.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository organizationRepository;

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
}
