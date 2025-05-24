package com.github.ikarita.server.security;

import org.springframework.security.core.Authentication;

 public class CommunityMethodSecurityExpressionRoot extends IkaritaSecurityExpressionRoot {
    public boolean hasCommunityAuthority(Authentication authentication, String communityId, String permission) {
        return userSecurityService.hasCommunityAuthority(authentication, communityId, permission);
    }
}
