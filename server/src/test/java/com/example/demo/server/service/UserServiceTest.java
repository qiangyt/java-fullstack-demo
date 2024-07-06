package com.example.demo.server.service;

import com.example.demo.sdk.ErrorCode;
import com.example.demo.sdk.req.SignUpReq;
import com.example.demo.server.dao.UserDao;
import com.example.demo.server.entity.UserEntity;

import io.github.qiangyt.common.err.BadRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserDao dao;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    SignUpReq signUpReq;

    @BeforeEach
    void setUp() {
        signUpReq = new SignUpReq();
        signUpReq.setName("test");
        signUpReq.setEmail("test@example.com");
        signUpReq.setPassword("password");
    }

    @Test
    void test_signUp_UserNameDuplicates() {
        when(dao.getByName(signUpReq.getName())).thenReturn(new UserEntity());

        var ex = assertThrows(BadRequest.class, () -> userService.signUp(signUpReq));
        assertEquals(ErrorCode.USER_NAME_DUPLICATES, ex.getCode());
        assertEquals("不能与已有用户名重复", ex.getMessage());
    }

    @Test
    void test_signUp_UserEmailDuplicates() {
        when(dao.getByEmail(signUpReq.getEmail())).thenReturn(new UserEntity());

        var ex = assertThrows(BadRequest.class, () -> userService.signUp(signUpReq));
        assertEquals(ErrorCode.USER_EMAIL_DUPLICATES, ex.getCode());
        assertEquals("邮箱已被注册", ex.getMessage());
    }

    @Test
    void test_signUp_Success() {
        when(dao.getByName(signUpReq.getName())).thenReturn(null);
        when(dao.getByEmail(signUpReq.getEmail())).thenReturn(null);

        var expected = new UserEntity();
        expected.setId("testId");
        expected.setName(signUpReq.getName());
        expected.setEmail(signUpReq.getEmail());
        expected.setPassword("hashedPassword");

        when(passwordEncoder.encode(signUpReq.getPassword())).thenReturn("hashedPassword");
        when(dao.save(any(UserEntity.class))).thenReturn(expected);

        var result = userService.signUp(signUpReq);

        assertNotNull(result);
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getName(), result.getName());
        assertEquals(expected.getEmail(), result.getEmail());
        assertEquals(expected.getPassword(), result.getPassword());

        verify(passwordEncoder).encode(signUpReq.getPassword());
        verify(dao).save(any(UserEntity.class));
    }

}