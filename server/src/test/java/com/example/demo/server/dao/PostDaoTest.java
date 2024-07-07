package com.example.demo.server.dao;

import io.github.qiangyt.common.misc.UuidHelper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Date;

import com.example.demo.server.entity.PostEntity;
import com.example.demo.server.entity.UserEntity;
import io.github.qiangyt.common.test.JpaDaoTestBase;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@EntityScan(basePackageClasses = PostEntity.class)
@EnableJpaRepositories(basePackageClasses = PostDao.class)
@ContextConfiguration(classes = PostDao.class)
public class PostDaoTest extends JpaDaoTestBase<PostEntity, String, PostDao> {

    public PostDaoTest(@Autowired PostDao dao) {
        super(dao);
    }

    @Disabled
    @Test
    void test_findOrderByCreatedAtDesc() {
        var em = getEntityManager();
        var milli = System.currentTimeMillis();

        var uid = UuidHelper.shortUuid();
        var u = UserEntity.builder().id(uid).name("u").email("u@example.com").build();
        em.persist(u);

        var m1 = PostEntity.builder().id(UuidHelper.shortUuid()).content("m1").createdBy(u).createdAt(new Date(milli - 1)).build();
        em.persist(m1);

        var m2 = PostEntity.builder().id(UuidHelper.shortUuid()).content("m2").createdBy(u).createdAt(new Date(milli + 1)).build();
        em.persist(m2);

        var m3 = PostEntity.builder().id(UuidHelper.shortUuid()).content("m3").createdBy(u).createdAt(new Date(milli)).build();
        em.persist(m3);

        var r = getDao().findAllOrderByCreatedAtDesc();
        assertNotNull(r);
        assertEquals(3, r.size());

        var actual2 = r.get(0);
        assertEquals(m2.getId(), actual2.getId());
        assertEquals(uid, actual2.getCreatedBy().getId());

        var actual3 = r.get(1);
        assertEquals(m3.getId(), actual3.getId());
        assertEquals(uid, actual3.getCreatedBy().getId());

        var actual1 = r.get(2);
        assertEquals(m1.getId(), actual1.getId());
        assertEquals(uid, actual1.getCreatedBy().getId());
    }

}
