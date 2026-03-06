package com.apple.meridian.exception;

/**
 * Thrown when a requested resource does not exist in the store.
 *
 * <p>Mapped to HTTP 404 by {@link com.apple.meridian.web.ExceptionHandlerAdvice}.
 * Raised automatically by {@code BaseDataModelService.findById} and by any service method
 * that resolves a foreign-key reference (e.g. looking up a publisher by id).</p>
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
