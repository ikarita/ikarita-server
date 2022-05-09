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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommunityServiceImpl implements CommunityService {
    private final CommunityRepository communityRepository;
    private final CommunityMapper communityMapper;

    @Override
    public CommunityDto createCommunity(NewCommunityDto newCommunityDto) {
        log.info("Saving community '{}'", newCommunityDto.getName());
        final Community communityEntity = communityRepository.save(communityMapper.asEntity(newCommunityDto));
        return communityMapper.asFullDto(communityEntity);
    }

    @Override
    public CommunityDto getCommunity(Long communityId) {
        log.info("Get community '{}'", communityId);
        final Optional<Community> community = communityRepository.findById(communityId);
        if(community.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Community with id '%d' does not exist.",
                    communityId
            ));
        }

        return communityMapper.asFullDto(community.get());
    }

    @Override
    public void deactivateCommunity(Long communityId) {
        final Optional<Community> community = communityRepository.findById(communityId);
        if(community.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to deactivate community with id '%d': Community does not exist.",
                    communityId
            ));
        }

        community.get().setActive(true);
    }

    @Override
    public List<CommunityDto> getCommunities() {
        return communityRepository.findAll().stream()
                .map(communityMapper::asFullDto)
                .collect(Collectors.toList());
    }
}
