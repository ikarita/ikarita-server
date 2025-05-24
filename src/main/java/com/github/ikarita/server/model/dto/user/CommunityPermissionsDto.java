package com.github.ikarita.server.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPermissionsDto {
    private boolean canEdit;
    private boolean canReview;
    private boolean canBanUser;
}
