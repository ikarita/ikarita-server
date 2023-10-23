package com.github.ikarita.server.security;

public class CommunityMethodSecurityExpressionRoot extends IkaritaSecurityExpressionRoot {
    public boolean hasCommunityAuthority(String communityId, String permission) {
        return true;
    }
}
