package com.example.demo.server.dao;

import io.github.qiangyt.common.misc.UuidHelper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.example.demo.server.entity.MessageEntity;
import com.example.demo.server.entity.UserEntity;
import io.github.qiangyt.common.test.JpaDaoTestBase;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@EntityScan(basePackageClasses = MessageEntity.class)
@EnableJpaRepositories(basePackageClasses = MessageDao.class)
@ContextConfiguration(classes = MessageDao.class)
public class MessageDaoTest extends JpaDaoTestBase<MessageEntity, String, MessageDao> {

    public MessageDaoTest(@Autowired MessageDao dao) {
        super(dao);
    }

    @Disabled
    @Test
    void test_findByPostIdIsNullc() {
        var em = getEntityManager();
        var milli = System.currentTimeMillis();

        var u1 = UserEntity.builder().id(UuidHelper.shortUuid()).name("u1").email("u1@example.com").build();
        em.persist(u1);
        var m1 = MessageEntity.builder().id(UuidHelper.shortUuid()).content("m1").createdBy(u1).createdAt(new Date(milli--)).postId(UuidHelper.shortUuid()).build();
        em.persist(m1);

        var u2 = UserEntity.builder().id(UuidHelper.shortUuid()).name("u2").email("u2@example.com").build();
        em.persist(u2);
        var m2 = MessageEntity.builder().id(UuidHelper.shortUuid()).content("m2").createdBy(u2).createdAt(new Date(milli--)).postId(null).build();
        em.persist(m2);

        var u3 = UserEntity.builder().id(UuidHelper.shortUuid()).name("u3").email("u3@example.com").build();
        em.persist(u3);
        var m3 = MessageEntity.builder().id(UuidHelper.shortUuid()).content("m3").createdBy(u3).createdAt(new Date(milli--)).postId(null).build();
        em.persist(m3);

        var r = getDao().findByPostIdIsNull();
        assertNotNull(r);
        assertEquals(2, r.size());

        var actual2 = r.get(0);
        assertEquals(m2.getId(), actual2.getId());
        assertEquals(u2.getId(), actual2.getCreatedBy().getId());

        var actual3 = r.get(1);
        assertEquals(m3.getId(), actual3.getId());
        assertEquals(u3.getId(), actual3.getCreatedBy().getId());
    }

    @Disabled
    @Test
    void test_findByPostIdIn() {
        var em = getEntityManager();
        var milli = System.currentTimeMillis();


        var u1 = UserEntity.builder().id(UuidHelper.shortUuid()).name("u1").email("u1@example.com").build();
        em.persist(u1);
        var m1 = MessageEntity.builder().id(UuidHelper.shortUuid()).content("m1").createdBy(u1).createdAt(new Date(milli--)).postId(UuidHelper.shortUuid()).build();
        em.persist(m1);

        var u2 = UserEntity.builder().id(UuidHelper.shortUuid()).name("u2").email("u2@example.com").build();
        em.persist(u2);
        var m2 = MessageEntity.builder().id(UuidHelper.shortUuid()).content("m2").createdBy(u2).createdAt(new Date(milli--)).postId(UuidHelper.shortUuid()).build();
        em.persist(m2);

        var u3 = UserEntity.builder().id(UuidHelper.shortUuid()).name("u3").email("u3@example.com").build();
        em.persist(u3);
        var m3 = MessageEntity.builder().id(UuidHelper.shortUuid()).content("m3").createdBy(u3).createdAt(new Date(milli--)).postId(UuidHelper.shortUuid()).build();
        em.persist(m3);

        var r = getDao().findByPostIdIn(List.of(m2.getPostId(), m1.getPostId()));
        assertNotNull(r);
        assertEquals(2, r.size());

        var actual1 = r.get(0);
        assertEquals(m1.getId(), actual1.getId());
        assertEquals(u1.getId(), actual1.getCreatedBy().getId());

        var actual2 = r.get(1);
        assertEquals(m2.getId(), actual2.getId());
        assertEquals(u2.getId(), actual2.getCreatedBy().getId());
    }

    @Test
    void test_findByPostIdIn_withEmptyCollection() {
        var em = getEntityManager();


        var u1 = UserEntity.builder().id(UuidHelper.shortUuid()).name("u1").email("u1@example.com").build();
        em.persist(u1);
        var m1 = MessageEntity.builder().id(UuidHelper.shortUuid()).content("m1").createdBy(u1).postId(UuidHelper.shortUuid()).build();
        em.persist(m1);
        
        var r = getDao().findByPostIdIn(Collections.emptyList());
        assertNotNull(r);
        assertTrue(r.isEmpty());
    }
}
