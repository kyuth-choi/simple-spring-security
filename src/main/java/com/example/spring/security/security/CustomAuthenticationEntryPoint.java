package com.example.spring.security.security;

import com.example.spring.security.vo.ResponseInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        log.debug("[BEG] CustomAuthenticationEntryPoint :: commence");

        ResponseInfo responseInfo = new ResponseInfo();
        String token = JwtTokenProvider.resolveToken(request);
        if (token != null) {
            responseInfo.setResponseCode(-2);
            responseInfo.setResponseMsg(JwtTokenProvider.getTokenErrorCode(token));
        } else {
            responseInfo.setResponseCode(-1);
            responseInfo.setResponseMsg("Access Denied");
        }

        // http status code 401 로 지정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        objectMapper.writeValue(response.getWriter(), responseInfo);
        log.debug("[END] CustomAuthenticationEntryPoint :: commence");
    }
}
