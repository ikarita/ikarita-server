package com.github.ikarita.server.security.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommunityPermission {
    DATA_READ("data:read"),
    DATA_ADD("data:add"),
    DATA_EDIT("data:edit"),
    DATA_DELETE("data:delete"),
    USER_READ("user:read"),
    USER_ADD("user:add"),
    USER_ROLE("user:role"),
    USER_DELETE("user:delete");

    private final String permission;
}
