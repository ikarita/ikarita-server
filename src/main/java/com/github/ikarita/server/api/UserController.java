package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.*;
import com.github.ikarita.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.github.ikarita.server.api.ApiUtils.jsonHeader;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(produces = "application/json")
    @Operation(
            tags = {"Users"}
    )
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(userService.getUsers());
    }

    @PostMapping(produces = "application/json")
    @Operation(
            tags = {"Users"}
    )
    public ResponseEntity<UserDto> createUser(@RequestBody NewUserDto user){
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(userService.createUser(user));
    }

    @DeleteMapping(path = "/ban/{userId}", produces = "application/json")
    @Operation(
            tags = {"Users"}
    )
    public ResponseEntity<UserDto> banUser(HttpServletRequest request, @PathVariable("userId") Long userId){
        userService.banUser(userId);
        return ResponseEntity
                .created(ApiUtils.getURI(request))
                .headers(jsonHeader())
                .build();
    }

    @PostMapping(path = "/roles", produces = "application/json")
    @Operation(
            tags = {"Users"}
    )
    public ResponseEntity<UserDto> addRoleToUser(@RequestBody NewCommunityRoleForUserDto roleForUser){
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(userService.addRoleToUser(roleForUser));
    }
}
