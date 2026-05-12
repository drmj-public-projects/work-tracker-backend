package com.drmj.work_tracker.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Utility class for extracting information from JWT token in the SecurityContext.
 * This class provides convenient methods to get user information from the current request.
 */
public class SecurityUtils {

    private SecurityUtils() {
        // Utility class, prevent instantiation
    }

    /**
     * Gets the current authenticated user's ID from the JWT token.
     *
     * @return UUID of the authenticated user
     * @throws IllegalStateException if no authentication is present
     */
    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        return UUID.fromString(authentication.getName());
    }

    /**
     * Gets the current authentication object from SecurityContext.
     *
     * @return Authentication object or null if not authenticated
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Checks if there is a currently authenticated user.
     *
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    public static String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }
        return authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replaceFirst("^ROLE_", ""))
                .orElseThrow(() -> new IllegalStateException("No role found"));
    }
}
