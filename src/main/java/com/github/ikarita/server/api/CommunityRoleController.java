package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.CommunityRoleDto;
import com.github.ikarita.server.model.dto.NewCommunityRoleDto;
import com.github.ikarita.server.service.CommunityRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.github.ikarita.server.api.ApiUtils.jsonHeader;

@RestController
@RequestMapping("/api/v1/community/roles")
@RequiredArgsConstructor
public class CommunityRoleController {
    private final CommunityRoleService communityRoleService;

    @PostMapping(produces = "application/json")
    @Operation(
            tags = {"Communities"},
            summary = "Creates roles available for a community.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Definition of the new community role without its ID."
            ),
            responses = {
                    @ApiResponse(
                            description = "Community role that was created."
                    )
            },
            security = {@SecurityRequirement(name = "Bearer JWT")}
    )
    public ResponseEntity<CommunityRoleDto> saveRole(HttpServletRequest request, @RequestBody NewCommunityRoleDto role){
        return ResponseEntity
                .created(ApiUtils.getURI(request))
                .headers(jsonHeader())
                .body(communityRoleService.createRole(role));
    }
}
