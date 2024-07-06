package com.example.demo.server.dao;

import io.github.qiangyt.common.misc.UuidHelper;

import static org.junit.jupiter.api.Assertions.assertNull;
import com.example.demo.server.entity.UserEntity;
import io.github.qiangyt.common.test.JpaDaoTestBase;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;

@EntityScan(basePackageClasses = UserEntity.class)
@EnableJpaRepositories(basePackageClasses = UserDao.class)
@ContextConfiguration(classes = UserDao.class)
public class UserDaoTest extends JpaDaoTestBase<UserEntity, String, UserDao> {

  public UserDaoTest(@Autowired UserDao dao) {
    super(dao);
  }

  @Test
  void test_getByName() {
    var em = getEntityManager();

    var u1 = UserEntity.builder().name("test_getByName_happy_1").email("test_getByName_happy_1@example.com")
        .id(UuidHelper.shortUuid()).build();
    em.persist(u1);

    var u2 = UserEntity.builder().name("test_getByName_happy_2").email("test_getByName_happy_2@example.com")
        .id(UuidHelper.shortUuid()).build();
    em.persist(u2);

    var actual = dao().getByName("test_getByName_happy_2");
    assertEntity(u2, actual);

    assertNull(dao().getByName("test_getByName_happy_x"));
  }

  @Test
  void test_getByEmail() {
    var em = getEntityManager();

    var u1 = UserEntity.builder().name("test_getByName_happy_1").email("test_getByName_happy_1@example.com")
        .id(UuidHelper.shortUuid()).build();
    em.persist(u1);

    var u2 = UserEntity.builder().name("test_getByName_happy_2").email("test_getByName_happy_2@example.com")
        .id(UuidHelper.shortUuid()).build();
    em.persist(u2);

    var actual = dao().getByEmail("test_getByName_happy_2@example.com");
    assertEntity(u2, actual);

    assertNull(dao().getByEmail("test_getByName_happy_x@example.com"));
  }

}
