package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.*;
import com.github.ikarita.server.model.entities.LocalUser;
import com.github.ikarita.server.model.mappers.UserMapper;
import com.github.ikarita.server.repository.UserRepository;
import com.github.ikarita.server.security.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(NewUserDto newUserDto) {
        log.info("Saving user '{}'", newUserDto.getUsername());
        newUserDto.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        final LocalUser localUserEntity = userRepository.save(userMapper.asEntity(newUserDto));
        return userMapper.asDto(localUserEntity);
    }

    @Override
    public UserDto addRoleToUser(NewCommunityRoleForUserDto roleForUser) {
        final String roleName = roleForUser.getRoleName();
        final String username = roleForUser.getUsername();

        log.info("Adding role {} to user {}", roleName, username);
        final Optional<LocalUser> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to add role '%s' to user '%s': User does not exist.",
                    roleName,
                    username
            ));
        }

        UserRole userRole;

        try {
            userRole = UserRole.valueOf(roleName);
        }
        catch (Exception e){
            throw new IllegalArgumentException(String.format(
                    "Failed to add role '%s' to user '%s': Role does not exist.",
                    roleName,
                    username
            ));
        }

        user.get().getUserRoles().add(userRole);
        return userMapper.asDto(user.get());
    }

    @Override
    public UserDto getUser(String username) {
        log.info("Getting user by name '{}'", username);
        final Optional<LocalUser> user = userRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to retrieve user '%s': User does not exist.",
                    username
            ));
        }

        return userMapper.asDto(user.get());
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("Getting all users");
        return userRepository.findAll().stream()
                .map(userMapper::asDto)
                .collect(Collectors.toList());
    }

    @Override
    public void banUser(Long userId) {
        final Optional<LocalUser> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to ban user '%s': User does not exist.",
                    userId
            ));
        }

        user.get().setBanned(true);
    }
}
