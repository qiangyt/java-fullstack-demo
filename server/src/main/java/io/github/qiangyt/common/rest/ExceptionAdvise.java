package io.github.qiangyt.common.rest;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import io.github.qiangyt.common.err.BadRequest;
import io.github.qiangyt.common.err.BaseError;
import io.github.qiangyt.common.err.InternalError;

import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Intercept and handle exceptions thrown by RestController. Output the thrown exceptions as HTTP response body.
 */
@lombok.extern.slf4j.Slf4j
@ControllerAdvice(annotations = { RestController.class })
public class ExceptionAdvise {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Map<String, Object>> handleError(Throwable ex) {
        var err = normalizeError(ex);

        var status = err.getStatus();
        if (err.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error(ex.getMessage(), ex);
        } else {
            log.warn(ex.getMessage(), ex);
        }

        return new ResponseEntity<>(err.toSpringMvcResponse(), status);
    }

    public static BaseError normalizeError(Throwable ex) {
        if (ex instanceof BaseError) {
            return ((BaseError) ex);
        }

        // TODO: handle InvocationTargetException and root cause
        if (ex instanceof MethodArgumentNotValidException) {
            List<ObjectError> objErrors = ((MethodArgumentNotValidException) ex).getBindingResult().getAllErrors();

            var errors = objErrors.stream().map(objErr -> objErr.getDefaultMessage()).collect(Collectors.toList());

            return new BadRequest(BadRequest.Code.METHOD_ARGUMENT_NOT_VALID, ex, "method argument not valid", errors);
        }

        if (ex instanceof ConstraintViolationException) {
            Set<ConstraintViolation<?>> violations = ((ConstraintViolationException) ex).getConstraintViolations();

            var errors = violations.stream().map(violation -> {
                return String.format("%s: %s", violation.getPropertyPath(), violation.getMessage());
            }).collect(Collectors.toList());

            return new BadRequest(BadRequest.Code.CONSTRAINT_VIOLATION, ex, "constraint violated", errors);
        }

        if (ex instanceof BindException || ex instanceof HttpMessageNotReadableException
                || ex instanceof MissingServletRequestParameterException
                || ex instanceof MissingServletRequestPartException || ex instanceof TypeMismatchException) {
            return new BadRequest(BadRequest.Code.OTHER);
        }

        if (ex instanceof HttpMediaTypeNotAcceptableException) {
            return new BaseError(HttpStatus.NOT_ACCEPTABLE, BaseError.Code.OTHER, ex);
        }
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            return new BaseError(HttpStatus.METHOD_NOT_ALLOWED, BaseError.Code.OTHER, ex);
        }
        if (ex instanceof HttpMediaTypeNotSupportedException) {
            return new BaseError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, BaseError.Code.OTHER, ex);
        }

        if (ex instanceof AsyncRequestTimeoutException) {
            return new BaseError(HttpStatus.REQUEST_TIMEOUT, BaseError.Code.OTHER, ex);
        }

        return new InternalError(ex);
    }

}
