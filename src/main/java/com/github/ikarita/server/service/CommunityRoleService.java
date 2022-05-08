package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.CommunityRoleDto;
import com.github.ikarita.server.model.dto.NewCommunityRoleDto;

public interface CommunityRoleService {
    CommunityRoleDto createRole(NewCommunityRoleDto role);
}
