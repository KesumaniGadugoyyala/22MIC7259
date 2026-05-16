package com.affordmed.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.affordmed.middleware.RateLimitFilter;
import com.affordmed.middleware.RequestIdFilter;
import com.affordmed.middleware.RequestLoggingFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final MockJwtAuthFilter mockJwtAuthFilter;
    private final RequestIdFilter requestIdFilter;
    private final RequestLoggingFilter requestLoggingFilter;
    private final RateLimitFilter rateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api-docs/**", "/swagger-ui/**", "/actuator/**").permitAll()
                .anyRequest().authenticated())
            .httpBasic(Customizer.withDefaults());

        http.addFilterBefore(requestIdFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(requestLoggingFilter, RequestIdFilter.class);
        http.addFilterAfter(mockJwtAuthFilter, RequestLoggingFilter.class);
        http.addFilterAfter(rateLimitFilter, MockJwtAuthFilter.class);

        return http.build();
    }
}
