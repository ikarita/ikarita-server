package com.github.ikarita.server.service.community;

import com.github.ikarita.server.model.dto.community.CommunityRoleDto;
import com.github.ikarita.server.model.dto.community.NewCommunityRoleDto;
import com.github.ikarita.server.model.entities.community.CommunityRole;
import com.github.ikarita.server.model.mappers.CommunityRoleMapper;
import com.github.ikarita.server.repository.community.CommunityRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommunityRoleServiceImpl implements CommunityRoleService {

    private final CommunityRoleRepository communityRoleRepository;
    private final CommunityRoleMapper communityRoleMapper;

    @Override
    public CommunityRoleDto createRole(NewCommunityRoleDto newCommunityRoleDto) {
        log.info("Saving role '{}'", newCommunityRoleDto.getName());
        final CommunityRole communityRoleEntity = communityRoleRepository.save(communityRoleMapper.asEntity(newCommunityRoleDto));
        return communityRoleMapper.asDto(communityRoleEntity);
    }
}
