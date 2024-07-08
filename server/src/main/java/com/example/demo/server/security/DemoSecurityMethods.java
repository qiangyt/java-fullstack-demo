package com.example.demo.server.security;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.server.entity.UserEntity;
import static com.example.demo.server.entity.UserEntity.USER_MAPPER;
import com.example.demo.server.service.UserService;

import io.github.qiangyt.common.security.AuthUser;
import io.github.qiangyt.common.security.SecurityMethods;

@Component
@lombok.Getter
@lombok.Setter
public class DemoSecurityMethods implements SecurityMethods<Object> {

    @Autowired
    UserService userService;

    @Override
    public AuthUser<Object> getUser(String userName) {
        UserEntity u;
        if (userName.contains("@")) {
            u = getUserService().getByEmail(userName);
        } else {
            u = getUserService().getByName(userName);
        }
        if (u == null) {
            return null;
        }
        return AuthUser.simple(userName, u.getPassword(), u);
    }

    @Override
    public Object buildSignInOkResponse(AuthUser<Object> user, String token) {
        return Map.of("user", USER_MAPPER.map((UserEntity) user.getExtra()), "token", token);
    }
}
