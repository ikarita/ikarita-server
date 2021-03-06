package com.github.ikarita.server.service.community;

import com.github.ikarita.server.model.dto.community.CommunityDto;
import com.github.ikarita.server.model.dto.community.NewCommunityDto;

import java.util.List;

public interface CommunityService {
    CommunityDto createCommunity(NewCommunityDto newCommunityDto);

    CommunityDto getCommunity(Long communityId);

    CommunityDto deactivateCommunity(Long communityId);

    List<CommunityDto> getCommunities();
}
