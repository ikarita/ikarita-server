package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.CommunityDto;
import com.github.ikarita.server.model.dto.NewCommunityDto;

public interface CommunityService {
    CommunityDto createCommunity(NewCommunityDto newCommunityDto);
}
