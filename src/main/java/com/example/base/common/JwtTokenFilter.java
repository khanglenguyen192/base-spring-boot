package com.example.base.common;

import com.example.base.constants.RolesConstant;
import com.example.base.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final ObjectMapper objectMapper;

    private static class SecurityAuthority implements GrantedAuthority {
        private String authority;
        private SecurityAuthority(String authority) {
            this.authority = authority;
        }

        @Override
        public String getAuthority() {
            return this.authority;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();

        if (servletPath.contains("swagger-ui") || servletPath.contains("swagger-resources")
                || servletPath.contains("v3/api-docs")) {
            filterChain.doFilter(request, response);
        } else {
            final String header = request.getHeader("authorization");
            log.debug(String.format("doFilterInternal - Header: {}"), header);

            if (StringUtils.isEmpty(header) || !header.startsWith("Beaarer ")) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not authorized");
                return;
            }

            List<String> roles = new ArrayList<>();

//            String email = JwtHelper.getUserName(header);
            String email = "khang.le@gmail.com";
            if (StringUtils.isNoneBlank(email)) {
                List<String> userRoles = userService.getRolesByMail(email);
                if (ObjectUtils.isNotEmpty(userRoles)) {
                    userRoles.addAll(userRoles);
                }
            } else {
                log.error("jwt.getClaims().get('username') is null");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unidentified username, please check authentication provider");
                return;
            }

            roles.add(RolesConstant.ROLE_USER);

            UsernamePasswordAuthenticationToken
                    authenticationToken = new UsernamePasswordAuthenticationToken(email, null, setupSecurityAuthority(roles));

            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);;
        }
    }

    private List<SecurityAuthority> setupSecurityAuthority(List<String> roles) {
        List<SecurityAuthority> securityAuthorities = new ArrayList<>();
        for (String role: roles) {
            securityAuthorities.add(new SecurityAuthority(role));
        }
        return securityAuthorities;
    }
}
