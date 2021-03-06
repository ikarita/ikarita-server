package com.github.ikarita.server.repository.user;

import com.github.ikarita.server.model.entities.user.LocalUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<LocalUser, Long> {
    Optional<LocalUser> findByUsername(String username);
}
