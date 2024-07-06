package com.example.demo.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.sdk.resp.UserResp;

import com.example.demo.sdk.req.MessageReq;
import com.example.demo.sdk.req.SignUpReq;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.security.core.Authentication;

/**
 * @author
 *
 */
@lombok.Getter
@lombok.Setter
@Validated
@RestController
@RequestMapping(path = "/rest", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
public class DemoApiV1Rest {

    @Autowired
    DemoApiV1Facade facade;

    @GetMapping("/users/_/{id}")
    public UserResp getUser(@PathVariable @NotBlank String id) {
        return getFacade().getUser(id);
    }

    @PostMapping("/signup")
    public UserResp signUp(@RequestBody @Valid SignUpReq req) {
        return getFacade().signUp(req);
    }

    @PostMapping(path = "/token", consumes = "*")
    public String generateJwt(Authentication auth) {
        return getFacade().generateJwt(auth);
    }

    // for test only
    @GetMapping("/hello")
    public String hello(Authentication auth) {
        return "Hello, " + auth.getName() + "!";
    }

    @PostMapping("/messages")
    public String postMessage(@RequestBody @Valid MessageReq req) {
        return getFacade().postMessage(req);
    }

}
