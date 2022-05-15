package com.github.ikarita.server.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserPermission {
    USER_READ("user:read"),
    USER_RESTRICT("user:restrict"),
    USER_BAN("user:ban"),
    COMMUNITY_READ("community:read"),
    COMMUNITY_ADD("community:add"),
    COMMUNITY_DELETE("community:delete");

    private final String permission;
}
