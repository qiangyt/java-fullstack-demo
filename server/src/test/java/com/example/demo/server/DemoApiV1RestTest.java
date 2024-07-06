package com.example.demo.server;

import io.github.qiangyt.common.misc.UuidHelper;
import io.github.qiangyt.common.test.RestTestBase;

import com.example.demo.sdk.resp.UserResp;
import com.example.demo.sdk.req.SignUpReq;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

import java.util.Date;

import org.springframework.test.context.ContextConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author
 *
 */
@Disabled
@ContextConfiguration(classes = DemoApiV1Rest.class)
@WebMvcTest(DemoApiV1Rest.class)
public class DemoApiV1RestTest extends RestTestBase {

    @MockBean
    DemoApiV1Facade facade;

    @Test
    void test_getUser() {
        getThenExpectOk(null, "/rest/users/_/1234567890");

        String id = UuidHelper.shortUuid();
        var user = UserResp.builder().id(id).name("test_getUser").email("test_getUser@example.com")
                .createdAt(new Date()).build();

        when(this.facade.getUser(id)).thenReturn(user);

        getThenExpectOk(user, "/rest/users/_/{id}", id);
    }

    @Test
    void test_signUp() {
        var req = SignUpReq.builder().email("test_createUser@example.com")
                    .name("test_createUser")
                    .password("test_createUser_password").build();

        var user = UserResp.builder().id(UuidHelper.shortUuid()).name("test_createUser").email("test_createUser@example.com")
                    .createdAt(new Date()).build();

        when(this.facade.signUp(req)).thenReturn(user);

        postThenExpectOk(req, user, "/rest/signup");
    }

}
