package com.github.ikarita.server.security;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

import static com.github.ikarita.server.security.UserPermission.*;

@AllArgsConstructor
@Getter
public enum UserRole {
    VIEWER(Sets.newHashSet(COMMUNITY_READ)),
    CONTRIBUTOR(Sets.newHashSet(COMMUNITY_READ, COMMUNITY_ADD)),
    MODERATOR(Sets.newHashSet(COMMUNITY_READ, USER_READ, USER_RESTRICT)),
    ADMIN(Sets.newHashSet(COMMUNITY_READ, USER_READ, USER_DELETE, COMMUNITY_DELETE));

    private final Set<UserPermission> permissions;
}
