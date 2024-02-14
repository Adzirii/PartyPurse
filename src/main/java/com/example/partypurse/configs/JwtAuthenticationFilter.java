package com.example.partypurse.configs;

import com.example.partypurse.services.InMemoryTokenBlacklist;
import com.example.partypurse.services.JwtService;
import com.example.partypurse.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.parser.Authorization;
import org.hibernate.annotations.Comment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_PREFIX = "Bearer";

    private final JwtService jwtService;
    private final UserService userService;
    private final InMemoryTokenBlacklist tokenBlacklist;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);


        // Token is valid and not blacklisted
        // Proceed with request processing

        if (Objects.isNull(authHeader) || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.substring(7);
        if (jwt != null && !tokenBlacklist.isBlacklisted(jwt)) {

        String subject = jwtService.extractUsername(jwt);

        Authentication authentication = getAuthentication(subject);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
}

private Authentication getAuthentication(final String subject) {
    UserDetails userDetails = userService.loadUserByUsername(subject);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
}
}
