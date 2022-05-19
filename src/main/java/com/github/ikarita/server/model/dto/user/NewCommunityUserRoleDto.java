package com.github.ikarita.server.model.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommunityUserRoleDto {
    private Long userId;
    private Long roleId;
    private Long communityId;
}
