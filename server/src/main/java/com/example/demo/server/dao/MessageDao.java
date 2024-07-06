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
public interface MessageDao extends MessageDaoEx, JpaDao<MessageEntity, String> {

    // @Query("SELECT e FROM MessageEntity e WHERE e.rootId IS NULL ORDER BY e.createdAt DESC")
    List<MessageEntity> findByRootIdIsNullOrderByCreatedAtDesc();

    @Query("SELECT e FROM MessageEntity e WHERE e.rootId IN :rootIds ORDER BY e.createdAt DESC")
    List<MessageEntity> findByRootIdInOrderByCreatedAtDesc(Collection<String> rootIds);

}
