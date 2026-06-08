package com.printforge.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // ── 1. Read the Authorization header ─────────────────────
        String authHeader = request.getHeader("Authorization");

        // ── 2. Check it starts with "Bearer " ────────────────────
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);  // no token, move on
            return;
        }

        // ── 3. Extract the token (remove "Bearer " prefix) ───────
        String token = authHeader.substring(7);

        // ── 4. Validate the token ─────────────────────────────────
        if (!jwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);  // bad token, move on
            return;
        }

        // ── 5. Extract username and register with Spring Security ─
        String username = jwtUtil.extractUsername(token);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        List.of()   // roles/authorities — empty for now
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        // ── 6. Tell Spring Security this request is authenticated ─
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ── 7. Continue to the next filter / controller ───────────
        filterChain.doFilter(request, response);
    }
}
