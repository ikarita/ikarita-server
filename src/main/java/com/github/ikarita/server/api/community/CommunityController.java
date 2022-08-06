package com.github.ikarita.server.api.community;

import com.github.ikarita.server.api.ApiUtils;
import com.github.ikarita.server.model.dto.community.CommunityDto;
import com.github.ikarita.server.model.dto.community.NewCommunityDto;
import com.github.ikarita.server.service.community.CommunityService;
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
@RequestMapping("/api/v1/communities")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping(produces = "application/json")
    @Operation(
            tags = {"Communities"},
            summary = "Fetches all communities.",
            responses = {
                    @ApiResponse(
                            description = "List of communities."
                    )
            },
            security = {@SecurityRequirement(name = "Bearer JWT")}
    )
    public ResponseEntity<List<CommunityDto>> getCommunities(){
        List<CommunityDto> communitiesDto = communityService.getCommunities();
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(communitiesDto);
    }

    @GetMapping(path = "/{communityId}", produces = "application/json")
    @Operation(
            tags = {"Communities"},
            summary = "Fetches community by its ID.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the community to be fetched.")
            },
            responses = {
                    @ApiResponse(
                            description = "Community corresponding to the ID."
                    )
            },
            security = {@SecurityRequirement(name = "Bearer JWT")}
    )
    public ResponseEntity<CommunityDto> getCommunity(@PathVariable("communityId") Long communityId){
        final CommunityDto communityDto = communityService.getCommunity(communityId);
        return ResponseEntity
                .ok()
                .headers(jsonHeader())
                .body(communityDto);
    }

    @PreAuthorize("hasAuthority('COMMUNITY_CREATE')")
    @PostMapping(produces = "application/json")
    @Operation(
            tags = {"Communities"},
            summary = "Creates community.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Definition of the new community without its ID."
            ),
            responses = {
                    @ApiResponse(
                            description = "Community that was created."
                    )
            },
            security = {@SecurityRequirement(name = "Bearer JWT")}
    )
    public ResponseEntity<CommunityDto> createCommunity(HttpServletRequest request, @RequestBody NewCommunityDto newCommunityDto){
        final CommunityDto communityDto = communityService.createCommunity(newCommunityDto);
        return ResponseEntity
                .created(ApiUtils.getURI(request))
                .headers(jsonHeader())
                .body(communityDto);
    }

    @PreAuthorize("hasAuthority('COMMUNITY_CREATE')")
    @PostMapping(path = "/deactivate/{communityId}", produces = "application/json")
    @Operation(
            tags = {"Communities"},
            summary = "Deactivates community by its ID.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the community to be deactivated.")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Deactivated community."
                    )
            },
            security = {@SecurityRequirement(name = "Bearer JWT")}
    )
    public ResponseEntity<CommunityDto> deactivateCommunity(HttpServletRequest request, @PathVariable("communityId") Long communityId){
        final CommunityDto communityDto = communityService.deactivateCommunity(communityId);
        return ResponseEntity
                .created(ApiUtils.getURI(request))
                .headers(jsonHeader())
                .body(communityDto);
    }
}
