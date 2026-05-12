package com.drmj.work_tracker.dto.response.user;

import com.drmj.work_tracker.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class UserResponse {
    private UUID id;
    private String name;
    private String email;
    private String timezone;

    public static UserResponse fromEntity(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .timezone(user.getTimezone())
                .build();
    }
}
