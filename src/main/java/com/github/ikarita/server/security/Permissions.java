package com.github.ikarita.server.security;

public class Permissions {
    private Permissions() {}

    public static final String[] ALLOWED_PATHS = new String[]{
            "/api/v1/token/access",
            "/api/v1/token/refresh"
    };
}
