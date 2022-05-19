package com.github.ikarita.server.repository.community;

import com.github.ikarita.server.model.entities.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Optional<Community> findByName(String name);
}
