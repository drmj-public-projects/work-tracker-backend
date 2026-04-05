package com.drmj.work_tracker.service;

import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.entity.WorkSession;

import java.util.UUID;

public interface WorkSessionService {
    WorkSession getById(UUID id);

    WorkSession startSession(StartWorkSessionRequest request);

    WorkSession endSession(WorkSession workSession);
}
