package com.github.ikarita.server.security.permissions;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.github.ikarita.server.security.permissions.UserPermission.*;

@AllArgsConstructor
@Getter
public enum UserRole {
    VIEWER(Sets.newHashSet(COMMUNITY_READ)),
    CONTRIBUTOR(Sets.newHashSet(COMMUNITY_READ, COMMUNITY_CREATE)),
    MODERATOR(Sets.newHashSet(COMMUNITY_READ, USER_LIST, USER_RESTRICT)),
    ADMIN(Sets.newHashSet(UserPermission.values()));

    private final Set<UserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> authorities = getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toSet());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));

        return authorities;
    }
}
