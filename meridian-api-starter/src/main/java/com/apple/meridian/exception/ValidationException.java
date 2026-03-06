package com.apple.meridian.exception;

/**
 * Thrown when a requested operation is semantically invalid for the entity's current state.
 *
 * <p>Mapped to HTTP 400 Bad Request by {@link com.apple.meridian.web.ExceptionHandlerAdvice}.
 * Typical uses: illegal status transitions (e.g. publishing an article that is not in REVIEW),
 * redundant state changes (e.g. deactivating a channel that is already inactive), and business
 * rule violations (e.g. submitting an article to a channel that is at full capacity).</p>
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}
