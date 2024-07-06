package com.example.demo.server.dao.jpa;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.qiangyt.common.jpa.AbstractJpaDao;

import com.example.demo.server.dao.UserDaoEx;
import com.example.demo.server.entity.QUserEntity;
import com.example.demo.server.entity.UserEntity;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl extends AbstractJpaDao<UserEntity, String> implements UserDaoEx {

    @Autowired
    public UserDaoImpl(EntityManager entityManager) {
        super(UserEntity.class, QUserEntity.userEntity, entityManager);
    }

}
