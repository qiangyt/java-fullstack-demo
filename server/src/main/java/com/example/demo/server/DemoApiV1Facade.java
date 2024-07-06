package com.example.demo.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.sdk.DemoApiV1;
import com.example.demo.sdk.resp.MessageResp;
import com.example.demo.sdk.resp.UserResp;
import com.example.demo.sdk.req.MessageReq;
import com.example.demo.sdk.req.SignUpReq;
import com.example.demo.server.entity.UserEntity;
import com.example.demo.server.service.MessageService;
import com.example.demo.server.service.UserService;

import io.github.qiangyt.common.security.AuthService;

@Service
@lombok.Setter
@lombok.Getter
@Transactional(propagation = Propagation.REQUIRED)
public class DemoApiV1Facade implements DemoApiV1 {

    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Autowired
    AuthService authService;

    @Override
    public UserResp signUp(SignUpReq req) {
        var u = getUserService().signUp(req);
        return UserEntity.USER_MAPPER.map(u);
    }

    @Override
    public UserResp getUser(String id) {
        var u = getUserService().getUser(false, id);
        return UserEntity.USER_MAPPER.map(u);
    }

    @Override
    public String postMessage(MessageReq req) {
        return getMessageService().postMessage(req);
    }

    @Override
    public List<MessageResp> listAllMessages() {
        return getMessageService().listAllMessages();
    }

    public String generateJwt(Authentication auth) {
        return getAuthService().generateJwt(auth);
    }

}
