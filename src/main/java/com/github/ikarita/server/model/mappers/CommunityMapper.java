package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.CommunityDto;
import com.github.ikarita.server.model.dto.CommunitySimpleDto;
import com.github.ikarita.server.model.dto.NewCommunityDto;
import com.github.ikarita.server.model.entities.Community;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    Community asEntity(CommunitySimpleDto communitySimpleDto);
    Community asEntity(NewCommunityDto newCommunityDto);
    Community asEntity(CommunityDto communityDto);

    CommunitySimpleDto asSimpleDto(Community community);
    CommunityDto asFullDto(Community community);
}