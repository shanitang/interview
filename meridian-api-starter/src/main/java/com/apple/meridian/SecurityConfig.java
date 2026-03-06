package com.apple.meridian;

/**
 * Centralises routing constants shared across the controller layer.
 *
 * <p>In a production service this class would also hold Spring Security configuration
 * (filter chains, authentication providers, CSRF rules, etc.). Here it is intentionally
 * minimal — just the API path prefix used by every {@code @RequestMapping}.</p>
 */
public class SecurityConfig {
    public static final String REST_API_PATH_PREFIX = "/api";
}
