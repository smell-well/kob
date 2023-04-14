package com.kob.backend.config.filter;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginExceptionEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("Now in login exception filter");

//        response.setContentType("application/json;charset=UTF-8");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("{\"message\": \"authentication failed\"}");

//        Map<String, String> map = new HashMap<>();
//        map.put("error_message", authException.getMessage());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error_message", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonObject.toJSONString());
    }
}
