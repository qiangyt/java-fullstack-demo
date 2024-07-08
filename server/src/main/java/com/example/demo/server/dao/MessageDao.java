package com.example.demo.server.dao;

import jakarta.annotation.Nullable;

import io.github.qiangyt.common.jpa.JpaDao;

import com.example.demo.server.entity.MessageEntity;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Nullable
public interface MessageDao extends JpaDao<MessageEntity, String> {

    @Query("SELECT e FROM MessageEntity e WHERE e.postId is null ORDER BY e.createdAt DESC")
    List<MessageEntity> findByPostIdIsNull();

    //@Query("SELECT e FROM CommentEntity e WHERE e.postId=:postId")
    //List<MessageEntity> findByPostId(String postId);

    //@Query("SELECT e FROM MessageEntity e WHERE e.postId in (:postId)")
    List<MessageEntity> findByPostIdIn(Collection<String> postId);

}
