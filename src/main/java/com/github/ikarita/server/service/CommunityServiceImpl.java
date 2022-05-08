package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.CommunityDto;
import com.github.ikarita.server.model.dto.NewCommunityDto;
import com.github.ikarita.server.model.entities.Community;
import com.github.ikarita.server.model.mappers.CommunityMapper;
import com.github.ikarita.server.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;

    @Override
    public CommunityDto createCommunity(NewCommunityDto newCommunityDto) {
        log.info("Saving role '{}'", newCommunityDto.getName());
        final Community communityEntity = communityRepository.save(communityMapper.asEntity(newCommunityDto));
        return communityMapper.asFullDto(communityEntity);
    }
}
