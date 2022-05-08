package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.*;
import com.github.ikarita.server.model.entities.CommunityRole;
import com.github.ikarita.server.model.entities.User;
import com.github.ikarita.server.model.mappers.RoleMapper;
import com.github.ikarita.server.model.mappers.UserMapper;
import com.github.ikarita.server.repository.RoleRepository;
import com.github.ikarita.server.repository.UserRepository;
import com.github.ikarita.server.security.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<User> user = userRepository.findByUsername(username);

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

    @Override
    public UserDto saveUser(NewUserDto newUserDto) {
        log.info("Saving user '{}'", newUserDto.getUsername());
        newUserDto.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        final User userEntity = userRepository.save(userMapper.newUserTdoToUser(newUserDto));
        return userMapper.userToUserDto(userEntity);
    }

    @Override
    public CommunityRoleDto saverRole(NewCommunityRoleDto newCommunityRoleDto) {
        log.info("Saving role '{}'", newCommunityRoleDto.getName());
        final CommunityRole communityRoleEntity = roleRepository.save(roleMapper.newRoleDtoToRole(newCommunityRoleDto));
        return roleMapper.roleToRoleDto(communityRoleEntity);
    }

    @Override
    public void addRoleToUser(NewCommunityRoleForUserDto roleForUser) {
        final String roleName = roleForUser.getRoleName();
        final String username = roleForUser.getUsername();

        log.info("Adding role {} to user {}", roleName, username);
        final Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to add role '%s' to user '%s': User does not exist.",
                    roleName,
                    username
            ));
        }

        final UserRole userRole = UserRole.valueOf(roleName);
        if(userRole == null){
            throw new IllegalArgumentException(String.format(
                    "Failed to add role '%s' to user '%s': Role does not exist.",
                    roleName,
                    username
            ));
        }

        user.get().getUserRoles().add(userRole);
    }

    @Override
    public UserDto getUser(String username) {
        log.info("Getting user by name '{}'", username);
        final Optional<User> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to retrieve user '%s': User does not exist.",
                    username
            ));
        }

        return userMapper.userToUserDto(user.get());
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDto)
                .collect(Collectors.toList());
    }
}
