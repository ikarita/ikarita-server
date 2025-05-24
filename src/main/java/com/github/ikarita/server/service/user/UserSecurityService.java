package com.github.ikarita.server.service.user;

import org.springframework.security.core.Authentication;

public interface UserSecurityService {
    boolean hasCommunityAuthority(Authentication authentication, String communityId, String permission);
}
