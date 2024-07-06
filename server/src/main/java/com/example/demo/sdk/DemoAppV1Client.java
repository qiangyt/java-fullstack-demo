package com.example.demo.sdk;

import com.example.demo.sdk.resp.MessageResp;
import com.example.demo.sdk.resp.UserResp;
import com.example.demo.sdk.req.MessageReq;
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
    public String postMessage(MessageReq req) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'postMessage'");
    }

    @Override
    public List<MessageResp> listAllMessages() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listAllMessages'");
    }

    @Override
    public UserResp getUser(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUser'");
    }

}
