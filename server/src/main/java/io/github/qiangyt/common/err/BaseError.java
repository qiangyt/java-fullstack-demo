package io.github.qiangyt.common.err;

import java.util.Date;

import org.springframework.http.HttpStatus;

import io.github.qiangyt.common.rest.ErrorResponse;

/**
 * Base class for all exception classes. Generally not used directly; typically, one of its subclasses is used instead.
 */
@lombok.Getter
public class BaseError extends RuntimeException {

    public enum Code {
        OTHER
    }

    private static final long serialVersionUID = -6956880450354038826L;

    /**
     * Error status
     */
    final HttpStatus status;

    /**
     * Error code
     */
    final Enum<?> code;

    /**
     * Error message parameters
     */
    final Object[] params;

    /**
     * Exception thrown when there is no cascading exception; allows specifying message format and message parameters.
     *
     * @param status
     *            Error status
     * @param code
     *            Error code
     * @param messageFormat
     *            Error message format
     * @param params
     *            Error message parameters
     *
     * @see String#format
     */
    public BaseError(HttpStatus status, Enum<?> code, String messageFormat, Object... params) {
        super(String.format(messageFormat, params));

        this.status = status;
        this.code = code;
        this.params = params;
    }

    /**
     * Exception thrown when there is no cascading exception; only simple message text can be specified.
     *
     * @param status
     *            Error status
     * @param code
     *            Error code
     * @param message
     *            Error message
     */
    public BaseError(HttpStatus status, Enum<?> code, String message) {
        super(message);

        this.status = status;
        this.code = code;
        this.params = new Object[0];
    }

    /**
     * Exception thrown when there is no cascading exception; message text not needed, as the error message represented
     * by the error code is used directly.
     *
     * @param status
     *            Error status
     * @param code
     *            Error code
     */
    public BaseError(HttpStatus status, Enum<?> code) {
        this(status, code, status.getReasonPhrase());
    }

    /**
     * Exception thrown when there is a cascading exception; allows specifying message format and error message
     * parameters.
     *
     * @param cause
     *            the cascading exception
     * @param status
     *            Error status
     * @param code
     *            Error code
     * @param messageFormat
     *            Error message format
     * @param params
     *            Error message parameters
     *
     * @see String#format
     */
    public BaseError(HttpStatus status, Enum<?> code, Throwable cause, String messageFormat, Object... params) {
        super(String.format(messageFormat, params), cause);

        this.status = status;
        this.code = code;
        this.params = params;
    }

    /**
     * Exception thrown when there is a cascading exception; only simple message text can be passed.
     *
     * @param cause
     *            the cascading exception
     * @param status
     *            Error status
     * @param code
     *            Error code
     * @param message
     *            Error message
     */
    public BaseError(HttpStatus status, Enum<?> code, Throwable cause, String message) {
        super(message, cause);

        this.status = status;
        this.code = code;
        this.params = new Object[0];
    }

    /**
     * Exception thrown when there is a cascading exception; message text not needed, as the error message represented
     * by the error code is used directly.
     *
     * @param cause
     *            the cascading exception
     * @param status
     *            Error status
     * @param code
     *            Error code
     */
    public BaseError(HttpStatus status, Enum<?> code, Throwable cause) {
        this(status, code, cause, status.getReasonPhrase());
    }

    public String getmessage() {
        return getMessage();
    }

    /**
     * Export to a Map. Cascading exceptions are not included. For RESTful APIs, this map serves as the HTTP response
     * body.
     */
    public ErrorResponse toResponse() {
        return ErrorResponse.builder().timestamp(new Date()).status(getStatus().value()).error(getMessage())
                .params(getParams()).build();
    }

}
