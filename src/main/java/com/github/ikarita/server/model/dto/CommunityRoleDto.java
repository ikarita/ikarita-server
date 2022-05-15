package com.github.ikarita.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommunityRoleDto {
    private Long id;
    private String name;
    private CommunityDto community;
}
