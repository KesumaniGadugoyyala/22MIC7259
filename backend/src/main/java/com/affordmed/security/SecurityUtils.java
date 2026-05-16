package com.affordmed.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.affordmed.exception.UnauthorizedException;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static String getStudentId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getPrincipal)
            .filter(StudentPrincipal.class::isInstance)
            .map(StudentPrincipal.class::cast)
            .map(StudentPrincipal::getStudentId)
            .orElseThrow(() -> new UnauthorizedException("Missing authentication"));
    }
}
