package com.apple.meridian.exception;

/**
 * Thrown when a create operation would produce a duplicate record.
 *
 * <p>Mapped to HTTP 409 Conflict by {@link com.apple.meridian.web.ExceptionHandlerAdvice}.
 * Services raise this in {@code beforeSave} when a uniqueness constraint would be violated
 * (e.g. two publishers sharing the same email, or two channels sharing the same name).</p>
 */
public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String message) {
        super(message);
    }
}
