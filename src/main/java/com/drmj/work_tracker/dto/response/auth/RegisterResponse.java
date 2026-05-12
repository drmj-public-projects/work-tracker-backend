package com.drmj.work_tracker.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class RegisterResponse {
    private UUID userId;
    private String name;
    private String email;
    private String message;
}