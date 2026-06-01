package com.drmj.work_tracker.dto.response.invitationCode;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvitationCodeStatsResponse {
    private Long activeCodes;
    private Long totalUses;
    private Long expiredCodes;
}
