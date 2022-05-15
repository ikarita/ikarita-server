package com.github.ikarita.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommunityRoleForUserDto {
    private Long userId;
    private Long roleId;
}
