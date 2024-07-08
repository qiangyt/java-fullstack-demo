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
import com.example.demo.sdk.req.CommentReq;
import com.example.demo.sdk.req.PostReq;
import com.example.demo.sdk.req.SignUpReq;
import com.example.demo.server.entity.UserEntity;
import com.example.demo.server.service.MessageService;
import com.example.demo.server.service.UserService;

import io.github.qiangyt.common.security.AuthService;
import io.github.qiangyt.common.security.AuthUser;

/**
 * The server-side API facade. 
 * Manages the transaction boundary, actually
 */
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

    public UserEntity currentUser() {
        AuthUser<UserEntity> authUser = getAuthService().currentUser();
        return authUser.getExtra();
    }

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
    public String newPost(PostReq req) {
        var creator = currentUser();
        return getMessageService().newPost(creator, req);
    }

    @Override
    public String newComment(String postId, CommentReq req) {
        getMessageService().ensurePostExists(postId);
        var creator = currentUser();
        return getMessageService().newComment(creator, postId, req);
    }

    @Override
    public List<MessageResp> listAllPosts() {
        return getMessageService().listAllPosts();
    }

    public String generateJwt(Authentication auth) {
        return getAuthService().generateJwt(auth);
    }

}
