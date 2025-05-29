package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.community.CommunityRoleDto;
import com.github.ikarita.server.model.dto.community.NewCommunityRoleDto;
import com.github.ikarita.server.model.entities.community.CommunityRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {CommunityMapper.class})
public interface CommunityRoleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    CommunityRole asEntity(NewCommunityRoleDto newCommunityRoleDto);


    CommunityRoleDto asDto(CommunityRole communityRole);
}
