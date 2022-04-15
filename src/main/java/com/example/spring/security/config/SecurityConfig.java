package com.example.spring.security.config;

import com.example.spring.security.security.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomLoginFailureHandler customLoginFailureHandler;

    private final CustomLoginSuccessHandler customLoginSuccessHandler;

    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(customAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/test/**");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // authProvider에서 로그인 성공시 이동할 Handler 설정
                .successHandler(customLoginSuccessHandler)
                // authProvider에서 로그인 실패시 이동할 Handler 설정
                .failureHandler(customLoginFailureHandler)
                // 해당 URL 요청이 들어오면 authProvider로 로그인 정보를 전달하여 로그인 로직 수행될 수 있도록 설정
                .loginProcessingUrl("/loginProcess")
                // 이 설정을 통해 사용자가 전달한 key 값이 아이디인지 알 수 있다.
                .usernameParameter("uid")
                // 이 설정을 통해 사용자가 전달한 key 값이 패스워드인지 알 수 있다.
                .passwordParameter("password")
                .and()
                .logout()
                // 로그아웃을 요청할 URL
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                // 로그아웃 성공시 이동할 Handler 설정
//                .logoutSuccessHandler(customLogoutSuccessHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    // CORS 적용
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));//허용할 URL
        configuration.addAllowedHeader("*");//허용할 Header
        configuration.addAllowedMethod("*");//허용할 Http Method
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
