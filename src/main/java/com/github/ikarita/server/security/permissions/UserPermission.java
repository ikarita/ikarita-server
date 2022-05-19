package com.github.ikarita.server.security.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserPermission {
    USER_LIST("user:list"),
    USER_RESTRICT("user:restrict"),
    USER_BAN("user:ban"),
    COMMUNITY_READ("community:read"),
    COMMUNITY_CREATE("community:create");
    private final String permission;
}
