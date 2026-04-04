package com.drmj.work_tracker.service;

import com.drmj.work_tracker.dto.request.workSession.StartWorkSessionRequest;
import com.drmj.work_tracker.entity.WorkSession;

public interface WorkSessionService {
    WorkSession startSession(StartWorkSessionRequest request);
}
