package com.github.ikarita.server.service.user;

import com.github.ikarita.server.model.entities.user.LocalUser;
import com.github.ikarita.server.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class LocalUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<LocalUser> user = userRepository.findByUsername(username);

        if(user.isEmpty()){
            final String message = String.format("User '%s' not found", username);
            log.error(message);
            throw new UsernameNotFoundException(message);
        }

        log.info("User '{}' found", username);

        return User.builder()
                .username(user.get().getUsername())
                .password(user.get().getPassword())
                .authorities(user.get().getUserRole().getGrantedAuthorities())
                .build();
    }
}
