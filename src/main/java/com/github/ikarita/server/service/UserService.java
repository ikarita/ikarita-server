package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.*;

import java.util.List;

public interface UserService {
    UserDto createUser(NewUserDto user);
    UserDto addRoleToUser(NewCommunityRoleForUserDto roleForUser);
    UserDto getUser(String username);
    List<UserDto> getUsers();

    void banUser(Long userId);
}
