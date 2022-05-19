package com.github.ikarita.server.service.community;

import com.github.ikarita.server.model.dto.community.CommunityRoleDto;
import com.github.ikarita.server.model.dto.community.NewCommunityRoleDto;

public interface CommunityRoleService {
    CommunityRoleDto createRole(NewCommunityRoleDto role);
}
