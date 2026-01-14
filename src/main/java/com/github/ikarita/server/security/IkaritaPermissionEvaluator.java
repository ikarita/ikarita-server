package com.github.ikarita.server.security;

import com.github.ikarita.server.service.user.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;

@RequiredArgsConstructor
public class IkaritaPermissionEvaluator implements PermissionEvaluator {
    private final UserSecurityService userSecurityService;

    @Override
    public boolean hasPermission(@NonNull Authentication authentication, @NonNull Object targetDomainObject, @NonNull Object permission) {
        throw new NotImplementedException("Need to check what is needed here");
    }

    @Override
    public boolean hasPermission(@NonNull Authentication authentication, @NonNull Serializable targetId, @NonNull String targetType, @NonNull Object permission) {
        if (permission instanceof String permissionName) {
            return userSecurityService.hasCommunityAuthority(authentication, targetType, permissionName);
        }

        return false;
    }
}
