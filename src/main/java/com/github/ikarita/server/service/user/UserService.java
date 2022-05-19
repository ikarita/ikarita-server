package com.github.ikarita.server.service.user;

import com.github.ikarita.server.model.dto.user.NewCommunityUserRoleDto;
import com.github.ikarita.server.model.dto.user.NewUserDto;
import com.github.ikarita.server.model.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserDto user);
    UserDto addCommunityRole(NewCommunityUserRoleDto roleForUser);
    UserDto getUser(String username);
    List<UserDto> getUsers();

    UserDto banUser(Long userId);
}
