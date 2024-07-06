package com.example.demo.server.dao.jpa;

import org.springframework.beans.factory.annotation.Autowired;

import io.github.qiangyt.common.jpa.AbstractJpaDao;

import com.example.demo.server.dao.MessageDaoEx;
import com.example.demo.server.entity.QMessageEntity;
import com.example.demo.server.entity.MessageEntity;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class MessageDaoImpl extends AbstractJpaDao<MessageEntity, String> implements MessageDaoEx {

    @Autowired
    public MessageDaoImpl(EntityManager entityManager) {
        super(MessageEntity.class, QMessageEntity.messageEntity, entityManager);
    }

}
