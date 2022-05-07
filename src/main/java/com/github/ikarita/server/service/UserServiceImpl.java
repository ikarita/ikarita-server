package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.*;
import com.github.ikarita.server.model.entities.Role;
import com.github.ikarita.server.model.entities.User;
import com.github.ikarita.server.repository.RoleRepository;
import com.github.ikarita.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDto saveUser(NewUserDto newUserDto) {
        log.info("Saving user '{}'", newUserDto.getUsername());
        final User userEntity = userRepository.save(Mapper.USER.manyToOne(newUserDto));
        return Mapper.USER.oneToMany(UserDto.class, userEntity);
    }

    @Override
    public RoleDto saverRole(NewRoleDto newRoleDto) {
        log.info("Saving role '{}'", newRoleDto.getName());
        final Role roleEntity = roleRepository.save(Mapper.ROLE.manyToOne(newRoleDto));
        return Mapper.ROLE.oneToMany(RoleDto.class, roleEntity);
    }

    @Override
    public void addRoleToUser(NewRoleForUserDto roleForUser) {
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

        final Optional<Role> role = roleRepository.findByName(roleName);
        if(role.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to add role '%s' to user '%s': Role does not exist.",
                    roleName,
                    username
            ));
        }

        user.get().getRoles().add(role.get());
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

        return Mapper.USER.oneToMany(UserDto.class, user.get());
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll().stream()
                .map(u -> Mapper.USER.oneToMany(UserDto.class, u))
                .collect(Collectors.toList());
    }
}
