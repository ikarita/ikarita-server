package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.*;
import com.github.ikarita.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody NewUserDto user){
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    @DeleteMapping(path = "/ban/{userId}")
    public ResponseEntity<UserDto> banUser(HttpServletRequest request, @PathVariable("userId") Long userId){
        userService.banUser(userId);
        return ResponseEntity.created(PathUtils.getURI(request)).build();
    }

    @PostMapping("/roles")
    public ResponseEntity<UserDto> addRoleToUser(@RequestBody NewCommunityRoleForUserDto roleForUser){
        return ResponseEntity.ok().body(userService.addRoleToUser(roleForUser));
    }
}
