package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.*;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserDto user);
    UserDto addCommunityRole(NewCommunityRoleForUserDto roleForUser);
    UserDto getUser(String username);
    List<UserDto> getUsers();

    UserDto banUser(Long userId);
}
