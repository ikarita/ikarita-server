package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.community.CommunityDto;
import com.github.ikarita.server.model.dto.community.CommunitySimpleDto;
import com.github.ikarita.server.model.dto.community.NewCommunityDto;
import com.github.ikarita.server.model.entities.community.Community;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    Community asEntity(CommunitySimpleDto communitySimpleDto);
    Community asEntity(NewCommunityDto newCommunityDto);
    Community asEntity(CommunityDto communityDto);

    CommunitySimpleDto asSimpleDto(Community community);
    CommunityDto asFullDto(Community community);
}