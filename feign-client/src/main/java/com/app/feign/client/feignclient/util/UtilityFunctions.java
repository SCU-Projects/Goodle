package com.app.feign.client.feignclient.util;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;

public class UtilityFunctions {

    public static MultiValueMap<String, String> getUserDetailsFromRequest(HttpServletRequest request)
    {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        headers.add("userName", request.getHeader("userName"));
        headers.add("password", request.getHeader("password"));
        return  headers;
    }

}
