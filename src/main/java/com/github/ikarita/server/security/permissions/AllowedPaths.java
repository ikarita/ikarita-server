package com.github.ikarita.server.security.permissions;

public class AllowedPaths {
    private AllowedPaths() {}

    public static String[] all(){
        return new String[]{
                "/api/v1/token/access",
                "/api/v1/token/refresh"
        };
    }

    public static String[] post(){
        return new String[]{
                "/api/v1/users"
        };
    }

    public static String[] swagger() {
        return new String[]{
                "/swagger-ui/**",
                "/v3/api-docs/**"
        };
    }
}
