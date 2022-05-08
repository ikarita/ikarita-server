package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.CommunityRoleDto;
import com.github.ikarita.server.model.dto.NewCommunityRoleDto;
import com.github.ikarita.server.model.entities.CommunityRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    CommunityRole newRoleDtoToRole(NewCommunityRoleDto newCommunityRoleDto);
    CommunityRoleDto roleToRoleDto(CommunityRole communityRole);
}
