package com.example.spring.security.security;

import com.example.spring.security.vo.ResponseInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    public CustomLoginFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("[BEG] CustomLoginFailureHandler :: onAuthenticationFailure");
        log.info("[로그인실패][{}]", exception.getMessage());

        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setResponseCode(-1);
        responseInfo.setResponseMsg("로그인이 실패하였습니다.");
        responseInfo.setData(exception.getMessage());

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        objectMapper.writeValue(response.getWriter(), responseInfo);

        log.info("[END] CustomLoginFailureHandler :: onAuthenticationFailure");
    }
}
