package com.example.demo.sdk;

import com.example.demo.sdk.resp.MessageResp;
import com.example.demo.sdk.resp.UserResp;
import com.example.demo.sdk.req.CommentReq;
import com.example.demo.sdk.req.PostReq;
import com.example.demo.sdk.req.SignUpReq;

import java.util.List;

/**
 *
 */
public interface DemoApiV1 {

    UserResp signUp(SignUpReq req);

    UserResp getUser(String id);

    MessageResp newPost(PostReq req);

    List<MessageResp> listAllPosts();

    MessageResp newComment(CommentReq req);

}
