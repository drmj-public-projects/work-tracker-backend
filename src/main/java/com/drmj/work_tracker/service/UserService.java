package com.drmj.work_tracker.service;

import com.drmj.work_tracker.entity.User;

import java.util.UUID;

public interface UserService {

    User getById(UUID id);

    User getReference(UUID id);
}
