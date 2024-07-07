package com.example.demo.server.dao;

import jakarta.annotation.Nullable;

import io.github.qiangyt.common.jpa.JpaDao;

import com.example.demo.server.entity.CommentEntity;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
@Nullable
public interface CommentDao extends CommentDaoEx, JpaDao<CommentEntity, String> {

    // @Query("SELECT e FROM CommentEntity e WHERE e.postId=:postId")
    List<CommentEntity> findByPostId(String postId);

}
