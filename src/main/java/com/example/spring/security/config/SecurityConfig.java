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
                // authProvider?????? ????????? ????????? ????????? Handler ??????
                .successHandler(customLoginSuccessHandler)
                // authProvider?????? ????????? ????????? ????????? Handler ??????
                .failureHandler(customLoginFailureHandler)
                // ?????? URL ????????? ???????????? authProvider??? ????????? ????????? ???????????? ????????? ?????? ????????? ??? ????????? ??????
                .loginProcessingUrl("/loginProcess")
                // ??? ????????? ?????? ???????????? ????????? key ?????? ??????????????? ??? ??? ??????.
                .usernameParameter("uid")
                // ??? ????????? ?????? ???????????? ????????? key ?????? ?????????????????? ??? ??? ??????.
                .passwordParameter("password")
                .and()
                .logout()
                // ??????????????? ????????? URL
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                // ???????????? ????????? ????????? Handler ??????
//                .logoutSuccessHandler(customLogoutSuccessHandler)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    // CORS ??????
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));//????????? URL
        configuration.addAllowedHeader("*");//????????? Header
        configuration.addAllowedMethod("*");//????????? Http Method
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
