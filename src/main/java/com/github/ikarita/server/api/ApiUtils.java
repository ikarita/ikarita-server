package com.github.ikarita.server.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

public class ApiUtils {
    private ApiUtils() {}

    public static URI getURI(HttpServletRequest request){
        return URI.create(request.getServletPath());
    }


    public static HttpHeaders jsonHeader(){
        final HttpHeaders httpHeaders= new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return httpHeaders;
    }
}
