package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.CommunityRoleDto;
import com.github.ikarita.server.model.dto.NewCommunityRoleDto;
import com.github.ikarita.server.service.CommunityRoleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static com.github.ikarita.server.api.ApiUtils.jsonHeader;

@RestController
@RequestMapping("/api/v1/community/roles")
@RequiredArgsConstructor
public class CommunityRoleController {
    private final CommunityRoleService communityRoleService;

    @PutMapping(produces = "application/json")
    @Operation(
            tags = {"Users", "Communities"}
    )
    public ResponseEntity<CommunityRoleDto> saveRole(@RequestBody NewCommunityRoleDto role){
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/roles/save").toUriString());
        return ResponseEntity
                .created(uri)
                .headers(jsonHeader())
                .body(communityRoleService.createRole(role));
    }
}
