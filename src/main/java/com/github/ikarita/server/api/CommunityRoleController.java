package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.CommunityRoleDto;
import com.github.ikarita.server.model.dto.NewCommunityRoleDto;
import com.github.ikarita.server.service.CommunityRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/api/v1/community/roles")
@RequiredArgsConstructor
public class CommunityRoleController {
    private final CommunityRoleService communityRoleService;

    @PostMapping("/create")
    public ResponseEntity<CommunityRoleDto> saveRole(@RequestBody NewCommunityRoleDto role){
        final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/v1/roles/save").toUriString());
        return ResponseEntity.created(uri).body(communityRoleService.createRole(role));
    }
}
