package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.*;

import java.util.List;

public interface UserService {
    UserDto saveUser(NewUserDto user);
    CommunityRoleDto saverRole(NewCommunityRoleDto role);
    void addRoleToUser(NewCommunityRoleForUserDto roleForUser);
    UserDto getUser(String username);
    List<UserDto> getUsers();
}
