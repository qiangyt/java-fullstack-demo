package com.example.demo.server.dao;

import io.github.qiangyt.common.misc.UuidHelper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import com.example.demo.server.entity.CommentEntity;
import com.example.demo.server.entity.UserEntity;
import io.github.qiangyt.common.test.JpaDaoTestBase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@EntityScan(basePackageClasses = CommentEntity.class)
@EnableJpaRepositories(basePackageClasses = CommentDao.class)
@ContextConfiguration(classes = CommentDao.class)
public class CommentDaoTest extends JpaDaoTestBase<CommentEntity, String, CommentDao> {

    public CommentDaoTest(@Autowired CommentDao dao) {
        super(dao);
    }

    @Test
    void test_findByPostId() {
        var em = getEntityManager();
        var milli = System.currentTimeMillis();

        var postId = UuidHelper.shortUuid();

        var uid = UuidHelper.shortUuid();
        var u = UserEntity.builder().id(uid).name("u").email("u@example.com").build();
        em.persist(u);

        // m1 is a comment of expected post
        var m1 = CommentEntity.builder().id(UuidHelper.shortUuid()).content("m1").createdBy(u).createdAt(new Date(milli--)).postId(postId).build();
        em.persist(m1);

        // m2 is a comment of expected post
        var m2 = CommentEntity.builder().id(UuidHelper.shortUuid()).content("m2").createdBy(u).createdAt(new Date(milli--)).postId(postId).build();
        em.persist(m2);

        // m3 is NOT a comment of expected post
        var m3 = CommentEntity.builder().id(UuidHelper.shortUuid()).content("m3").createdBy(u).createdAt(new Date(milli--)).postId(UuidHelper.shortUuid()).build();
        em.persist(m3);

        var r = getDao().findByPostId(postId);
        assertNotNull(r);
        assertEquals(2, r.size());

        var actual1 = r.get(0);
        assertEquals(m1.getId(), actual1.getId());
        assertEquals(uid, actual1.getCreatedBy().getId());

        var actual2 = r.get(1);
        assertEquals(m2.getId(), actual2.getId());
        assertEquals(uid, actual2.getCreatedBy().getId());
    }

}
