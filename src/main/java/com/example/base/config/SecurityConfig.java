package com.example.base.config;

import com.example.base.common.JwtTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(corsConfig -> corsConfig.disable())
                .csrf(csrfConfig -> csrfConfig.disable());

        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.exceptionHandling(excetionConfig -> excetionConfig.authenticationEntryPoint(
                ((request, response, authException) -> {
                    response.sendError(
                            HttpServletResponse.SC_UNAUTHORIZED,
                            authException.getMessage()
                    );
                })
        ));

        http.authorizeRequests(authorizeConfig -> authorizeConfig.anyRequest()
                .permitAll());

        return http.build();
    }
}
