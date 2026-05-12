package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.User;

import java.util.UUID;

public interface UserService {

    User getById(UUID id);

    User getReference(UUID id);

    User save(User user);

    boolean existsByEmail(String email);

    User update(UUID userId, String name, String timezone);
}
