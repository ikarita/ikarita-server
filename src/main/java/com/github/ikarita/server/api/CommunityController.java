package com.github.ikarita.server.api;

import com.github.ikarita.server.model.dto.CommunityDto;
import com.github.ikarita.server.model.dto.NewCommunityDto;
import com.github.ikarita.server.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/api/v1/communities")
@RequiredArgsConstructor
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping
    public ResponseEntity<List<CommunityDto>> getCommunities(){
        return ResponseEntity.ok().body(communityService.getCommunities());
    }

    @GetMapping(path = "/{communityId}")
    public ResponseEntity<CommunityDto> getCommunity(@PathVariable("communityId") Long communityId){
        return ResponseEntity.ok().body(communityService.getCommunity(communityId));
    }

    @PostMapping
    public ResponseEntity<CommunityDto> createCommunity(@RequestBody NewCommunityDto communityDto){
        return ResponseEntity.ok().body(communityService.createCommunity(communityDto));
    }

    @PostMapping(path = "/deactivate/{communityId}")
    public ResponseEntity<CommunityDto> deactivateCommunity(HttpServletRequest request, @PathVariable("communityId") Long communityId){
        communityService.deactivateCommunity(communityId);
        return ResponseEntity.created(PathUtils.getURI(request)).build();
    }
}
