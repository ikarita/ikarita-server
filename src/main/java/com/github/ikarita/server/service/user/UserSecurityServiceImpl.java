package com.github.ikarita.server.service.user;

import com.github.ikarita.server.model.entities.community.Community;
import com.github.ikarita.server.model.entities.community.CommunityRole;
import com.github.ikarita.server.model.entities.user.CommunityUser;
import com.github.ikarita.server.model.entities.user.LocalUser;
import com.github.ikarita.server.repository.community.CommunityRepository;
import com.github.ikarita.server.repository.user.CommunityUserRepository;
import com.github.ikarita.server.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserSecurityServiceImpl implements UserSecurityService {
    private final UserRepository userRepository;
    private final CommunityRepository communityRepository;
    private final CommunityUserRepository communityUserRepository;


    @Override
    public boolean hasCommunityAuthority(Authentication authentication, String communityId, String permission) {
        final String name = authentication.getName();
        final Optional<LocalUser> user = userRepository.findByUsername(name);
        if(user.isEmpty()) {
          return false;
        }
        final Optional<Community> community = communityRepository.findByName(communityId);
        if(community.isEmpty()){
            return false;
        }
        Optional<CommunityUser> communityUser = communityUserRepository.findByLocalUserAndCommunity(user.get(), community.get());
        if(communityUser.isEmpty()){
            return false;
        }

        Collection<CommunityRole> communityRoles = communityUser.get().getCommunityRoles();
        return false;
    }
}
