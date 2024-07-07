package com.example.demo.server;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.sdk.DemoApiV1;
import com.example.demo.sdk.resp.CommentResp;
import com.example.demo.sdk.resp.PostResp;
import com.example.demo.sdk.resp.UserResp;
import com.example.demo.sdk.req.CommentReq;
import com.example.demo.sdk.req.PostReq;
import com.example.demo.sdk.req.SignUpReq;
import com.example.demo.server.entity.UserEntity;
import com.example.demo.server.service.CommentService;
import com.example.demo.server.service.PostService;
import com.example.demo.server.service.UserService;

import io.github.qiangyt.common.security.AuthService;
import io.github.qiangyt.common.security.AuthUser;

/**
 * The server-side API facade. Manages the transaction boundary, actually
 */
@Service
@lombok.Setter
@lombok.Getter
@Transactional(propagation = Propagation.REQUIRED)
public class DemoApiV1Facade implements DemoApiV1 {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

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
        return getPostService().newPost(creator, req);
    }

    @Override
    public List<PostResp> listAllPosts() {
        return getPostService().listAllPosts();
    }

    @Override
    public String newComment(String postId, CommentReq req) {
        getPostService().ensurePostExists(postId);
        var creator = currentUser();
        return getCommentService().newComment(creator, postId, req);
    }

    @Override
    public List<CommentResp> listComments(String postId) {
        getPostService().ensurePostExists(postId);
        return getCommentService().listComments(postId);
    }

    public String generateJwt(Authentication auth) {
        return getAuthService().generateJwt(auth);
    }

}
