package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.*;
import com.github.ikarita.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/users/save")
    public ResponseEntity<UserDto> saveUser(@RequestBody NewUserDto user){
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/users/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/roles/save")
    public ResponseEntity<CommunityRoleDto> saveRole(@RequestBody NewCommunityRoleDto role){
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/roles/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saverRole(role));
    }

    @PostMapping("/users/role/save")
    public ResponseEntity<?> addRoleToUser(@RequestBody NewCommunityRoleForUserDto roleForUser){
        userService.addRoleToUser(roleForUser);
        return ResponseEntity.ok().build();
    }
}
