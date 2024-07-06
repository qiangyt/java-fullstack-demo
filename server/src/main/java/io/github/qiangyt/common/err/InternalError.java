package io.github.qiangyt.common.err;

import org.springframework.http.HttpStatus;

/**
 * HTTP INTERNAL_SERVER_ERROR
 */
public class InternalError extends BaseError {

    public enum Code {
        OTHER
    }

    private static final long serialVersionUID = -5209197321059638276L;

    public InternalError(String messageFormat, Object... params) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, Code.OTHER, messageFormat, params);
    }

    public InternalError(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, Code.OTHER, message);
    }

    public InternalError(Throwable cause, String messageFormat, Object... params) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, Code.OTHER, cause, messageFormat, params);
    }

    public InternalError(Throwable cause, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, Code.OTHER, cause, message);
    }

    public InternalError(Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, Code.OTHER, cause);
    }

}
