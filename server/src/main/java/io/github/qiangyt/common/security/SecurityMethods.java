package io.github.qiangyt.common.security;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

import io.github.qiangyt.common.rest.ErrorResponse;

public interface SecurityMethods<E> {

    AuthUser<E> getUser(String name);

    Object buildSignInOkResponse(AuthUser<E> user, String token);

    default ErrorResponse buildSignInErrorResponse(AuthenticationException failed) {
        return ErrorResponse.builder().timestamp(new Date()).error(failed.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value()).build();
    }

}
