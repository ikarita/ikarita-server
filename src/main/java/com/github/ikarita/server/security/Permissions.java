package com.github.ikarita.server.security;

public class Permissions {
    private Permissions() {}

    public static final String[] ALLOWED_PATHS = new String[]{
            "/api/v1/token/access",
            "/api/v1/token/refresh"
    };

    public static final String[] ALLOWED_POST_PATHS = new String[]{
            "/api/v1/users"
    };

    public static final String[] SWAGGER_PATHS = new String[]{
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };
}
