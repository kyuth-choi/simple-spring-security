package com.example.spring.security.security;

import com.example.spring.security.vo.LoginInfo;
import com.example.spring.security.vo.ResponseInfo;
import com.example.spring.security.entity.UserInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    public CustomLoginSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        log.info("[BEG] CustomLoginSuccessHandler :: onAuthenticationSuccess");
        LoginInfo loginInfo = (LoginInfo) authentication.getPrincipal();
        Set<GrantedAuthority> authorities = new HashSet<>(authentication.getAuthorities());
        String roles = StringUtils.join(authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()), ',');

        ResponseInfo responseInfo = new ResponseInfo();
        String token = JwtTokenProvider.createToken(loginInfo, roles);

        if (token == null) {
            responseInfo.setResponseCode(-1);
            responseInfo.setResponseMsg("로그인이 실패하였습니다.");
        } else {
            loginInfo.setRoles(roles);
            loginInfo.setToken(token);
            responseInfo.setData(loginInfo);
        }

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("utf-8");
        objectMapper.writeValue(response.getWriter(), responseInfo);

        log.info("[END] CustomLoginSuccessHandler :: onAuthenticationSuccess");
    }
}
