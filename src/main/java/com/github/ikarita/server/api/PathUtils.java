package com.github.ikarita.server.api;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

public class PathUtils {
    private PathUtils() {}

    public static URI getURI(HttpServletRequest request){
        return URI.create(request.getServletPath());
    }
}
