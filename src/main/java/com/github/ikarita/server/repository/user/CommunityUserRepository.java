package com.github.ikarita.server.repository.user;

import com.github.ikarita.server.model.entities.community.Community;
import com.github.ikarita.server.model.entities.user.CommunityUser;
import com.github.ikarita.server.model.entities.user.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
    Optional<CommunityUser> findByLocalUserAndCommunity(LocalUser user, Community community);
}
