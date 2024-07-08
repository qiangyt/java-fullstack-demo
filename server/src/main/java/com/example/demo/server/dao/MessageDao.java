package com.example.demo.server.dao;

import jakarta.annotation.Nullable;

import io.github.qiangyt.common.jpa.JpaDao;

import com.example.demo.server.entity.MessageEntity;

import org.springframework.stereotype.Repository;

@Repository
@Nullable
public interface MessageDao extends JpaDao<MessageEntity, String> {

    // @Query("SELECT e FROM MessageEntity e WHERE e.parentId IS NULL ORDER BY e.createdAt DESC")
    // List<MessageEntity> findPosts();

    // @Query("SELECT e FROM MessageEntity e WHERE e.postId in (:postId) AND e.parentId IS NOT NULL ORDER BY e.createdAt
    // DESC")
    // List<MessageEntity> findCommentsByPostId(Collection<String> postId);

}
