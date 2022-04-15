package com.example.spring.security.security;

import com.example.spring.security.repository.UserInfoRepository;
import com.example.spring.security.entity.UserInfo;
import com.example.spring.security.vo.LoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserInfoRepository userInfoRepository;

    @Autowired
    public CustomAuthenticationProvider(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.info("[BEG] CustomAuthenticationProvider");
        // 로그인 정보 추출
        String uid = authentication.getName();
        String password = (String) authentication.getCredentials();

        log.info("id : {}, password : {}", uid, password);

        UserInfo userInfo = userInfoRepository.findByAccountIdAndAccountPw(uid, password);

        if (userInfo != null) {
            Set<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if (userInfo.getUserLevel() == 0) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }
            log.info("[END] CustomAuthenticationProvider");
            return new UsernamePasswordAuthenticationToken(new LoginInfo(userInfo), password, authorities);
        } else {
            throw new BadCredentialsException("로그인에 실패하였습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
