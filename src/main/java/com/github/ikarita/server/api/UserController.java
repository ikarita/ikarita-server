package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.*;
import com.github.ikarita.server.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.github.ikarita.server.api.ApiUtils.jsonHeader;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('user:list')")
    @GetMapping(produces = "application/json")
    @Operation(
            tags = {"Users"},
            summary = "Fetches all users.",
            responses = {
                    @ApiResponse(
                            description = "List of users."
                    )
            },
            security = {@SecurityRequirement(name = "Bearer JWT")}
    )
    public ResponseEntity<List<UserDto>> getUsers() {
        final List<UserDto> usersDto = userService.getUsers();
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(usersDto);
    }

    @PostMapping(produces = "application/json")
    @Operation(
            tags = {"Users"},
            summary = "Creates user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Definition of the new user without its ID."
            ),
            responses = {
                    @ApiResponse(
                            description = "User that was created."
                    )
            }
    )
    public ResponseEntity<UserDto> createUser(@RequestBody NewUserDto user){
        final UserDto userDto = userService.createUser(user);
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(userDto);
    }

    @PreAuthorize("hasAuthority('user:ban')")
    @DeleteMapping(path = "/ban/{userId}", produces = "application/json")
    @Operation(
            tags = {"Users"},
            summary = "Bans user by its ID.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the user to be banned."),
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User that is banned from the platform."
                    )
            },
            security = {@SecurityRequirement(name = "Bearer JWT")}
    )
    public ResponseEntity<UserDto> banUser(HttpServletRequest request, @PathVariable("userId") Long userId){
        final UserDto userDto = userService.banUser(userId);
        return ResponseEntity
                .created(ApiUtils.getURI(request))
                .headers(jsonHeader())
                .body(userDto);
    }

    @PreAuthorize("@communitySecurityService.hasAuthority(#roleForUser.getCommunityId(), 'user:role')")
    @PostMapping(path = "communities/roles", produces = "application/json")
    @Operation(
            tags = {"Users", "Communities"},
            summary = "Assigns community role to user within a community.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "IDs of the user and the community role to be assigned to it."
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User with new community role."
                    )
            },
            security = {@SecurityRequirement(name = "Bearer JWT")}
    )
    public ResponseEntity<UserDto> addRoleToUser(HttpServletRequest request, @RequestBody NewCommunityRoleForUserDto roleForUser){
        final UserDto userDto = userService.addCommunityRole(roleForUser);
        return ResponseEntity
                .created(ApiUtils.getURI(request))
                .headers(jsonHeader())
                .body(userDto);
    }
}
