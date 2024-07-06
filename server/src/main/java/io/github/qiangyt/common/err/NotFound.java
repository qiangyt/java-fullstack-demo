package io.github.qiangyt.common.err;

import org.springframework.http.HttpStatus;

/**
 * HTTP NOT_FOUND
 */
public class NotFound extends BaseError {

    public enum Code {
        OTHER, ENTITY_NOT_FOUND
    }

    private static final long serialVersionUID = -6244828726201482351L;

    public NotFound(Enum<?> code, String messageFormat, Object... params) {
        super(HttpStatus.NOT_FOUND, code, messageFormat, params);
    }

    public NotFound(Enum<?> code, String message) {
        super(HttpStatus.NOT_FOUND, code, message);
    }

    public NotFound(Enum<?> code) {
        super(HttpStatus.NOT_FOUND, code);
    }

    public NotFound(Enum<?> code, Throwable cause, String messageFormat, Object... params) {
        super(HttpStatus.NOT_FOUND, code, cause, messageFormat, params);
    }

    public NotFound(Enum<?> code, Throwable cause, String message) {
        super(HttpStatus.NOT_FOUND, code, cause, message);
    }

    public NotFound(Enum<?> code, Throwable cause) {
        super(HttpStatus.NOT_FOUND, code, cause);
    }

}
