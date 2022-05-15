package com.github.ikarita.server.repository;

import com.github.ikarita.server.model.entities.Community;
import com.github.ikarita.server.model.entities.CommunityUser;
import com.github.ikarita.server.model.entities.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityUserRepository extends JpaRepository<CommunityUser, Long> {
    Optional<CommunityUser> findByLocalUserAndCommunity(LocalUser user, Community community);
}
