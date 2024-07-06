package io.github.qiangyt.common.err;

import org.springframework.http.HttpStatus;

/**
 * HTTP FORBIDDEN
 */
public class Forbidden extends BaseError {

    public static enum Code {
        OTHER, CONTEXT_NOT_FOUND, USER_ID_NOT_FOUND_IN_CONTEXT,
    }

    /**
     *
     */
    private static final long serialVersionUID = -4509198240300211902L;

    public Forbidden(Enum<?> code, String messageFormat, Object... params) {
        super(HttpStatus.FORBIDDEN, code, messageFormat, params);
    }

    public Forbidden(Enum<?> code, String message) {
        super(HttpStatus.FORBIDDEN, code, message);
    }

    public Forbidden(Enum<?> code) {
        super(HttpStatus.FORBIDDEN, code);
    }

    public Forbidden(Enum<?> code, Throwable cause, String messageFormat, Object... params) {
        super(HttpStatus.FORBIDDEN, code, cause, messageFormat, params);
    }

    public Forbidden(Enum<?> code, Throwable cause, String message) {
        super(HttpStatus.FORBIDDEN, code, cause, message);
    }

    public Forbidden(Enum<?> code, Throwable cause) {
        super(HttpStatus.FORBIDDEN, code, cause);
    }
}
