package com.github.ikarita.server.model.dto.community;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCommunityRoleDto {
    private String name;
    private CommunitySimpleDto community;
}
