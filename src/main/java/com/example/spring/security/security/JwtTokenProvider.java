package com.example.spring.security.security;

import com.example.spring.security.entity.UserInfo;
import com.example.spring.security.vo.LoginInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.lang.Objects;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class JwtTokenProvider {

    private static final String _SECRET_KEY = "security-test";
    private static final long _EXPIRE_TIME = 36000000;
    private static final String _HEADER_KEY = "x-api-key";


    @Getter
    enum claimSet {
        _ROLES("roles", String.class),
        _ID("accountId", String.class),
        _USER_NO("userNo", Long.class),
        _USER_NAME("userName", String.class);

        private final String value;
        private final Class<?> clazz;

        claimSet(String value, Class<?> clazz) {
            this.value = value;
            this.clazz = clazz;
        }

        public <T> T getClazz() {
            return (T) clazz;
        }
    }

    // JWT 토큰에서 인증 정보 조회
    public static Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser().setSigningKey(_SECRET_KEY).parseClaimsJws(token).getBody();
        Set<GrantedAuthority> authorities = Arrays.stream(claims.get(claimSet._ROLES.value).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUserNo(claims.get(claimSet._USER_NO.getValue(), claimSet._USER_NO.getClazz()));
        loginInfo.setId(claims.get(claimSet._ID.value, claimSet._ID.getClazz()));
        loginInfo.setUserName(claims.get(claimSet._USER_NAME.value, claimSet._USER_NAME.getClazz()));
        loginInfo.setRoles(claims.get(claimSet._ROLES.value, claimSet._ROLES.getClazz()));

        return new UsernamePasswordAuthenticationToken(loginInfo, token, authorities);
    }

    public static String createToken(LoginInfo userInfo, String roles) {

        if (userInfo != null) {
            Claims claims = Jwts.claims().setSubject(userInfo.getId());
            if (userInfo.getUserNo() != null) {
                claims.put(claimSet._USER_NO.value, userInfo.getUserNo());
            }
            if (userInfo.getId() != null) {
                claims.put(claimSet._ID.value, userInfo.getId());
            }
            if (userInfo.getUserName() != null) {
                claims.put(claimSet._USER_NAME.value, userInfo.getUserName());
            }
            if (roles != null) {
                claims.put(claimSet._ROLES.value, roles);
            }

            Date now = new Date();
            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + _EXPIRE_TIME))
                    .signWith(SignatureAlgorithm.HS256, _SECRET_KEY)
                    .compact();
        } else {
            return null;
        }
    }

    // 토큰 에러 코드 확인
    public static String getTokenErrorCode(String jwtToken) {
        try {
            Jwts.parser().setSigningKey(_SECRET_KEY).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "잘못된 요청 입니다.";
    }

    // Request의 Header에서 token 값 추출
    public static String resolveToken(HttpServletRequest request) {
        return request.getHeader(_HEADER_KEY);
    }

    // 토큰의 유효성 + 만료일자 확인
    public static boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(_SECRET_KEY).parseClaimsJws(jwtToken);
            for (claimSet claimName : claimSet.values()) {
                if (claims.getBody().get(claimName.value) == null) {
                    return false;
                }
            }
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
