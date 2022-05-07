package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.NewRoleDto;
import com.github.ikarita.server.model.dto.RoleDto;
import com.github.ikarita.server.model.entities.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role newRoleDtoToRole(NewRoleDto newRoleDto);
    RoleDto roleToRoleDto(Role role);
}
