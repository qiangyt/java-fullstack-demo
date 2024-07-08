package com.example.demo.sdk;

import com.example.demo.sdk.resp.MessageResp;
import com.example.demo.sdk.resp.UserResp;
import com.example.demo.sdk.req.CommentReq;
import com.example.demo.sdk.req.PostReq;
import com.example.demo.sdk.req.SignUpReq;

import java.util.List;

// TODO
public class DemoAppV1Client implements DemoApiV1 {

    final String token;

    public DemoAppV1Client(String token) {
        this.token = token;
    }

    @Override
    public UserResp signUp(SignUpReq req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'signUp'");
    }

    @Override
    public UserResp getUser(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

    @Override
    public String newPost(PostReq req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newPost'");
    }

    @Override
    public List<MessageResp> listAllPosts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listAllPosts'");
    }

    @Override
    public String newComment(String postId, CommentReq req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'newComment'");
    }
    
}
