package com.example.demo.server.dao.jpa;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.qiangyt.common.jpa.AbstractJpaDao;

import com.example.demo.server.dao.CommentDaoEx;
import com.example.demo.server.entity.QCommentEntity;
import com.example.demo.server.entity.CommentEntity;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class CommentDaoImpl extends AbstractJpaDao<CommentEntity, String> implements CommentDaoEx {

    @Autowired
    public CommentDaoImpl(EntityManager entityManager) {
        super(CommentEntity.class, QCommentEntity.commentEntity, entityManager);
    }

}
