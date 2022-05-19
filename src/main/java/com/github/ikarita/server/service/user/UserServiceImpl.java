package com.github.ikarita.server.service.user;

import com.github.ikarita.server.model.dto.user.NewCommunityUserRoleDto;
import com.github.ikarita.server.model.dto.user.NewUserDto;
import com.github.ikarita.server.model.dto.user.UserDto;
import com.github.ikarita.server.model.entities.community.CommunityRole;
import com.github.ikarita.server.model.entities.user.CommunityUser;
import com.github.ikarita.server.model.entities.community.CommunityUserId;
import com.github.ikarita.server.model.entities.user.LocalUser;
import com.github.ikarita.server.model.mappers.UserMapper;
import com.github.ikarita.server.repository.community.CommunityRoleRepository;
import com.github.ikarita.server.repository.user.CommunityUserRepository;
import com.github.ikarita.server.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CommunityRoleRepository communityRoleRepository;
    private final CommunityUserRepository communityUserRepository;
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
    public UserDto addCommunityRole(NewCommunityUserRoleDto roleForUser) {
        final Long userId = roleForUser.getUserId();
        final Long roleId = roleForUser.getRoleId();

        log.info("Adding role {} to user {}", userId, roleId);
        final Optional<LocalUser> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to add role '%s' to user '%s': User does not exist.",
                    roleId,
                    userId
            ));
        }

        final Optional<CommunityRole> role = communityRoleRepository.findById(userId);
        if(role.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to add role '%s' to user '%s': Community role does not exist.",
                    roleId,
                    userId
            ));
        }

        final CommunityUser communityUser = communityUserRepository.findByLocalUserAndCommunity(user.get(), role.get().getCommunity())
                .orElseGet(() ->{
                    CommunityUser c = new CommunityUser(
                            new CommunityUserId(user.get().getId(), role.get().getCommunity().getId()),
                            user.get(),
                            role.get().getCommunity(),
                            new ArrayList<>()
                    );
                    return communityUserRepository.save(c);
                });

        communityUser.getCommunityRoles().add(role.get());
        user.get().getCommunities().add(communityUser);

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
    public UserDto banUser(Long userId) {
        final Optional<LocalUser> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new IllegalArgumentException(String.format(
                    "Failed to ban user '%s': User does not exist.",
                    userId
            ));
        }

        user.get().setBanned(true);
        return userMapper.asDto(user.get());
    }
}
