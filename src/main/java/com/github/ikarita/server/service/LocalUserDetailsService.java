package com.github.ikarita.server.service;

import com.github.ikarita.server.model.entities.LocalUser;
import com.github.ikarita.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

        final List<SimpleGrantedAuthority> authorities = user.get().getUserRoles().stream()
                .map(r -> new SimpleGrantedAuthority(r.name()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.get().getUsername(),
                user.get().getPassword(),
                authorities
        );
    }
}
