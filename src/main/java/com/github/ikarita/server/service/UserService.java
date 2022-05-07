package com.github.ikarita.server.service;

import com.github.ikarita.server.model.dto.*;

import java.util.List;

public interface UserService {
    UserDto saveUser(NewUserDto user);
    RoleDto saverRole(NewRoleDto role);
    void addRoleToUser(NewRoleForUserDto roleForUser);
    UserDto getUser(String username);
    List<UserDto> getUsers();
}
