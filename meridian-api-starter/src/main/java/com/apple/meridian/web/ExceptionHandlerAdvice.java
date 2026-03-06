package com.apple.meridian.web;

import com.apple.meridian.exception.AlreadyExistsException;
import com.apple.meridian.exception.NotFoundException;
import com.apple.meridian.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Translates typed service exceptions into consistent JSON error responses.
 *
 * <p>Every controller method declares {@code throws Exception}. Rather than wrapping each
 * call in a try-catch, this {@code @ControllerAdvice} intercepts exceptions thrown anywhere
 * in the request processing chain and maps them to the appropriate HTTP status:</p>
 * <ul>
 *   <li>{@link com.apple.meridian.exception.NotFoundException} → 404 Not Found</li>
 *   <li>{@link com.apple.meridian.exception.AlreadyExistsException} → 409 Conflict</li>
 *   <li>{@link com.apple.meridian.exception.ValidationException} → 400 Bad Request</li>
 *   <li>{@link IllegalArgumentException} → 400 Bad Request (e.g. malformed UUIDs)</li>
 *   <li>Any other {@link Exception} → 500 Internal Server Error</li>
 * </ul>
 *
 * <p>The response body is always an {@link ApiError} with {@code errorCode} set to the
 * HTTP reason phrase and {@code errorMessage} set to the root-cause message.</p>
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ExceptionHandler(value = {AlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<ApiError> conflict(Exception exception) {
        LOGGER.warn(HttpStatus.CONFLICT.getReasonPhrase(), exception);
        return new ResponseEntity<>(new ApiError(HttpStatus.CONFLICT.getReasonPhrase(), exception), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ApiError> notFound(Exception exception) {
        return new ResponseEntity<>(new ApiError(HttpStatus.NOT_FOUND.getReasonPhrase(), exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            ValidationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiError> badRequest(Exception exception) {
        return new ResponseEntity<>(new ApiError(HttpStatus.BAD_REQUEST.getReasonPhrase(), exception), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            Exception.class,
            RuntimeException.class
    })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ApiError> internalServiceError(Exception exception) {
        LOGGER.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception);
        return new ResponseEntity<>(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), exception), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
