package com.apple.meridian.web;

import com.google.common.base.Throwables;

/**
 * Wire format for all error responses returned by this API.
 */
public class ApiError {
    public String errorCode;
    public String errorMessage;
    public String errorStackTrace;

    public ApiError(String errorCode, Exception ex) {
        this.errorCode = errorCode;
        if (ex != null) {
            this.errorMessage = Throwables.getRootCause(ex).getMessage();
            this.errorStackTrace = Throwables.getStackTraceAsString(ex);
        }
    }
}
