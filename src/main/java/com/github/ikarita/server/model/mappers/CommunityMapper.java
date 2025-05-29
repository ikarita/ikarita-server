package com.github.ikarita.server.model.mappers;

import com.github.ikarita.server.model.dto.community.CommunityDto;
import com.github.ikarita.server.model.dto.community.CommunitySimpleDto;
import com.github.ikarita.server.model.dto.community.NewCommunityDto;
import com.github.ikarita.server.model.entities.community.Community;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    @Mapping(target = "public", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "geoData", ignore = true)
    @Mapping(target = "metadataSchema", ignore = true)
    Community asEntity(CommunitySimpleDto communitySimpleDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "geoData", ignore = true)
    @Mapping(target = "metadataSchema", ignore = true)
    Community asEntity(NewCommunityDto newCommunityDto);

    @Mapping(target = "active", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "geoData", ignore = true)
    @Mapping(target = "metadataSchema", ignore = true)
    Community asEntity(CommunityDto communityDto);

    CommunitySimpleDto asSimpleDto(Community community);

    CommunityDto asFullDto(Community community);
}