package io.github.qiangyt.common.err;

import org.springframework.http.HttpStatus;

/**
 * HTTP BAD_REQUEST
 */
public class BadRequest extends BaseError {

    private static final long serialVersionUID = -7899971579299772118L;

    public enum Code {
        OTHER, METHOD_ARGUMENT_NOT_VALID, CONSTRAINT_VIOLATION, WRONG_FORMAT,
    }

    public BadRequest(Enum<?> code, String messageFormat, Object... params) {
        super(HttpStatus.BAD_REQUEST, code, messageFormat, params);
    }

    public BadRequest(Enum<?> code, String message) {
        super(HttpStatus.BAD_REQUEST, code, message);
    }

    public BadRequest(Enum<?> code) {
        super(HttpStatus.BAD_REQUEST, code);
    }

    public BadRequest(Enum<?> code, Throwable cause, String messageFormat, Object... params) {
        super(HttpStatus.BAD_REQUEST, code, cause, messageFormat, params);
    }

    public BadRequest(Enum<?> code, Throwable cause, String message) {
        super(HttpStatus.BAD_REQUEST, code, cause, message);
    }

    public BadRequest(Enum<?> code, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, code, cause);
    }

}
