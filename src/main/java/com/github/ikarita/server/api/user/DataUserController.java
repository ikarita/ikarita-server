package com.github.ikarita.server.api.user;

import com.github.ikarita.server.api.ApiUtils;
import com.github.ikarita.server.model.dto.user.NewCommunityUserRoleDto;
import com.github.ikarita.server.model.dto.user.UserDto;
import com.github.ikarita.server.service.user.DataUserService;
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
public class DataUserController {
    private final DataUserService dataUserService;

    @PreAuthorize("hasAuthority('USER_LIST')")
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
        final List<UserDto> usersDto = dataUserService.getUsers();
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(usersDto);
    }

    @PreAuthorize("hasAuthority('USER_BAN')")
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
        final UserDto userDto = dataUserService.banUser(userId);
        return ResponseEntity
                .created(ApiUtils.getURI(request))
                .headers(jsonHeader())
                .body(userDto);
    }

    @PreAuthorize("hasAuthority('USER_ROLE')")
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
    public ResponseEntity<UserDto> addRoleToUser(HttpServletRequest request, @RequestBody NewCommunityUserRoleDto roleForUser){
        final UserDto userDto = dataUserService.addCommunityRole(roleForUser);
        return ResponseEntity
                .created(ApiUtils.getURI(request))
                .headers(jsonHeader())
                .body(userDto);
    }
}
