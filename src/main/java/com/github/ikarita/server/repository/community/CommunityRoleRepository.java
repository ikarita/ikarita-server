package com.github.ikarita.server.repository.community;

import com.github.ikarita.server.model.entities.community.CommunityRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRoleRepository extends JpaRepository<CommunityRole, Long> {
    Optional<CommunityRole> findByName(String name);
}
