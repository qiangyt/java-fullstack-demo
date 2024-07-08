package io.github.qiangyt.common.security;

import io.github.qiangyt.common.json.JacksonHelper;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

// TODO：改用accessToken + refreshToken
@lombok.Getter
public class JwtAuthFilter extends UsernamePasswordAuthenticationFilter {

    final AuthenticationManager authManager;

    final AuthService authService;

    final ObjectMapper objectMapper;

    final SecurityMethods<Object> methods;

    public JwtAuthFilter(AuthenticationManager authManager, AuthService authService, ObjectMapper objectMapper,
            SecurityMethods<Object> methods) {
        this.authManager = authManager;
        this.authService = authService;
        this.objectMapper = objectMapper;
        this.methods = methods;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            var body = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
            var c = JacksonHelper.from(body, UserCredentials.class);
            if (c == null) {
                return null;
                // res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                // res.setContentType("application/json");
                // var errorResponse = Map.of("error", "Request body is missing");
                // res.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }
            return authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(c.getName(), c.getPassword(), Collections.emptyList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Handle successful authentication (e.g., generate token)
    @SuppressWarnings("unchecked")
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse resp, FilterChain chain,
            Authentication auth) throws IOException {
        var user = (AuthUser<Object>) auth.getPrincipal();

        var token = getAuthService().generateJwt(auth);
        var result = getMethods().buildSignInOkResponse(user, token);

        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        resp.setStatus(HttpStatus.OK.value());

        var w = resp.getWriter();
        w.write(getObjectMapper().writeValueAsString(result));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse resp,
            AuthenticationException failed) throws IOException {
        var result = getMethods().buildSignInErrorResponse(failed);

        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());

        var w = resp.getWriter();
        w.write(getObjectMapper().writeValueAsString(result));
    }
}

class UserCredentials {

    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
