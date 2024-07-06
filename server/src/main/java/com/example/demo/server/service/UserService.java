package com.example.demo.server.service;

import io.github.qiangyt.common.err.BadRequest;
import io.github.qiangyt.common.misc.UuidHelper;

import com.example.demo.sdk.ErrorCode;
import com.example.demo.sdk.req.SignUpReq;
import com.example.demo.server.dao.UserDao;
import com.example.demo.server.entity.UserEntity;
import static com.example.demo.server.entity.UserEntity.USER_MAPPER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@lombok.Getter
@lombok.Setter
public class UserService {

    @Autowired
    UserDao dao;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserEntity getUser(boolean ensureExists, String id) {
        return getDao().get(ensureExists, id);
    }

    public UserEntity getByName(String name) {
        return getDao().getByName(name);
    }

    public UserEntity getByEmail(String email) {
        return getDao().getByEmail(email);
    }

    public UserEntity signUp(SignUpReq req) {
        if (getByName(req.getName()) != null) {
            throw new BadRequest(ErrorCode.USER_NAME_DUPLICATES, "不能与已有用户名重复");
        }
        if (getByEmail(req.getEmail()) != null) {
            throw new BadRequest(ErrorCode.USER_EMAIL_DUPLICATES, "邮箱已被注册");
        }

        var r = USER_MAPPER.map(req);
        r.setId(UuidHelper.shortUuid());

        var hashedPassword = getPasswordEncoder().encode(req.getPassword());
        r.setPassword(hashedPassword);

        return getDao().save(r);
    }

}
