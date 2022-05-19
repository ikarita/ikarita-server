package com.github.ikarita.server.service.community;

import com.github.ikarita.server.model.entities.user.LocalUser;
import com.github.ikarita.server.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommunitySecurityService {
    private final UserRepository userRepository;

    public boolean hasAuthority(Long communityId, String authority){
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String username = authentication.getName();
        final Optional<LocalUser> user = userRepository.findByUsername(username);

        if(user.isEmpty()){
            return false;
        }

        return user.get().getCommunities().stream()
                .filter(c -> Objects.equals(c.getCommunity().getId(), communityId))
                .flatMap(c -> c.getCommunityRoles().stream())
                .flatMap(r -> r.getPermissions().stream())
                .anyMatch(p -> p.getPermission().equals(authority));
    }
}
