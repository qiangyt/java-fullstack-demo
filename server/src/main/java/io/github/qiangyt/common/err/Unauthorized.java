package io.github.qiangyt.common.err;

import org.springframework.http.HttpStatus;

/**
 * HTTP UNAUTHORIZED
 */
public class Unauthorized extends BaseError {

    public enum Code {
        OTHER
    }

    /**
     *
     */
    private static final long serialVersionUID = -6453279321344702468L;

    public Unauthorized(Enum<?> code, String messageFormat, Object... params) {
        super(HttpStatus.UNAUTHORIZED, code, messageFormat, params);
    }

    public Unauthorized(Enum<?> code, String message) {
        super(HttpStatus.UNAUTHORIZED, code, message);
    }

    public Unauthorized(Enum<?> code) {
        super(HttpStatus.UNAUTHORIZED, code);
    }

    public Unauthorized(Enum<?> code, Throwable cause, String messageFormat, Object... params) {
        super(HttpStatus.UNAUTHORIZED, code, cause, messageFormat, params);
    }

    public Unauthorized(Enum<?> code, Throwable cause, String message) {
        super(HttpStatus.UNAUTHORIZED, code, cause, message);
    }

    public Unauthorized(Enum<?> code, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, code, cause);
    }

}
