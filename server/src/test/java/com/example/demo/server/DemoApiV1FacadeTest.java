package com.example.demo.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import com.example.demo.server.entity.UserEntity;
import com.example.demo.server.service.UserService;

import io.github.qiangyt.common.security.AuthService;

public class DemoApiV1FacadeTest {

    @Mock
    UserService userService;

    @Mock
    AuthService authService;

    @InjectMocks
    DemoApiV1Facade target;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    void testSignUp() {
        var id = "testId";
        var req = new SignUpReq();
        req.setName("testUser");
        req.setPassword("testPass");

        var userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName(req.getName());

        when(userService.signUp(id, req)).thenReturn(userEntity);

        var result = target.signUp(req);

        assertEquals(id, result.getId());
        assertEquals(req.getName(), result.getName());

        verify(userService).signUp(id, req);
    }*/

    @Test
    void test_getUser() {
        var id = "testId";
        var userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setName("testUser");

        var user = UserEntity.USER_MAPPER.map(userEntity);

        when(userService.getUser(false, id)).thenReturn(userEntity);

        var result = target.getUser(id);

        assertEquals(id, result.getId());
        assertEquals(user.getName(), result.getName());

        verify(userService).getUser(false, id);
    }

    @Test
    void test_generateJwt() {
        var auth = mock(Authentication.class);

        var token = "testToken";

        when(authService.generateJwt(auth)).thenReturn(token);

        var result = target.generateJwt(auth);

        assertEquals(token, result);

        verify(authService).generateJwt(auth);
    }

}