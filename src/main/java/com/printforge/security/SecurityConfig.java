package com.printforge.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // ── Disable CSRF (not needed for JWT APIs) ────────────
                .csrf(csrf -> csrf.disable())

                // ── Disable sessions (JWT is stateless) ───────────────
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // ── Define public and protected routes ────────────────
                .authorizeHttpRequests(auth -> auth

                        // Public pages
                        .requestMatchers("/", "/index.html", "/orders.html",
                                "/login.html", "/admin.html",
                                "/style.css", "/**/*.css", "/**/*.js").permitAll()

                        // Public API endpoints
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/products").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/orders").permitAll()      // ← add this
                        .requestMatchers(HttpMethod.GET,  "/api/orders/customer").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/orders/**").permitAll()

                        // Everything else requires a valid token
                        .anyRequest().authenticated()
                )

                // ── Add JwtFilter before Spring's auth filter ─────────
                .addFilterBefore(jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ── Password encoder — BCrypt is the industry standard ───────
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ── AuthenticationManager — used by login endpoint ───────────
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
