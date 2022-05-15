package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.CommunityDto;
import com.github.ikarita.server.model.dto.NewCommunityDto;

import java.util.List;

public interface CommunityService {
    CommunityDto createCommunity(NewCommunityDto newCommunityDto);

    CommunityDto getCommunity(Long communityId);

    CommunityDto deactivateCommunity(Long communityId);

    List<CommunityDto> getCommunities();
}
