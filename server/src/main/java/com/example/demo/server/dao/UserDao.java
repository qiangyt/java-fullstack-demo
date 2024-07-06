package com.example.demo.server.dao;

import jakarta.annotation.Nullable;

import io.github.qiangyt.common.jpa.JpaDao;

import com.example.demo.server.entity.UserEntity;

import org.springframework.stereotype.Repository;

@Repository
@Nullable
public interface UserDao extends UserDaoEx, JpaDao<UserEntity, String> {

    UserEntity getByName(String name);

    UserEntity getByEmail(String email);

}
