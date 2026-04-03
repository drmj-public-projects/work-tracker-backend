package com.drmj.work_tracker.service;

import com.drmj.work_tracker.repository.OrganizationRepository;
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
}
