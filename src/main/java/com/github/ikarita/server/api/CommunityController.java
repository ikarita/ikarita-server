package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.CommunityDto;
import com.github.ikarita.server.model.dto.NewCommunityDto;
import com.github.ikarita.server.service.CommunityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.github.ikarita.server.api.ApiUtils.jsonHeader;

@RestController
@RequestMapping("/api/v1/communities")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping(produces = "application/json")
    @Operation(
            tags = {"Communities"},
            summary = "Fetches all communities."
    )
    public ResponseEntity<List<CommunityDto>> getCommunities(){
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(communityService.getCommunities());
    }

    @GetMapping(path = "/{communityId}", produces = "application/json")
    @Operation(
            tags = {"Communities"},
            summary = "Fetches a community by its ID."
    )
    public ResponseEntity<CommunityDto> getCommunity(@PathVariable("communityId") Long communityId){
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(communityService.getCommunity(communityId));
    }

    @PostMapping(produces = "application/json")
    @Operation(
            tags = {"Communities"},
            summary = "Creates a community."
    )
    public ResponseEntity<CommunityDto> createCommunity(@RequestBody NewCommunityDto communityDto){
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(communityService.createCommunity(communityDto));
    }

    @PostMapping(path = "/deactivate/{communityId}", produces = "application/json")
    @Operation(
            tags = {"Communities"},
            summary = "Deactivates a community a community."
    )
    public ResponseEntity<CommunityDto> deactivateCommunity(HttpServletRequest request, @PathVariable("communityId") Long communityId){
        communityService.deactivateCommunity(communityId);
        return ResponseEntity
                .created(ApiUtils.getURI(request))
                .headers(jsonHeader())
                .build();
    }
}
