package io.github.qiangyt.common.test;

import java.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.github.qiangyt.common.jpa.JpaDao;
import io.github.qiangyt.common.json.JacksonHelper;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Base class for unit testing JPA DAOs, includes utility methods for data import and entity object data validation.
 * 
 * Following the practice of JPA repositories in Spring, transactions are controlled by Spring to rollback after test case execution, ensuring isolation of test data between cases.
 *
 * The target database for testing is H2 (in-memory database), which is sufficient for most cases. If there are native SQL tests, they should be placed in integration tests.
 *
 * @param E - entity class type
 * @param ID - id type of entity class
 * @param D - DAO class
 */
@Disabled
@DataJpaTest
@EnableJpaAuditing
@lombok.Getter
public class JpaDaoTestBase<E, ID, D extends JpaDao<E, ID>> {

  @Autowired
  TestEntityManager entityManager;

  final D dao;

  protected JpaDaoTestBase(D dao) {
    this.dao = dao;
  }

  protected TestEntityManager entityManager() {
    return this.entityManager;
  }

  protected D dao() {
    return this.dao;
  }

  /**
   * Verify if two sets of entity objects have identical data.
   *
   * @param assertId Whether to validate the id values of the entity objects
   * @param actuals A set of entity objects to be validated
   * @param expecteds A set of entity objects to compare against (expected values)
   */
  @SuppressWarnings("unchecked")
  public void assertEntities(List<E> actuals, E... expecteds) {
    assertEntities(actuals, Arrays.asList(expecteds));
  }


  /**
   * Verify if two sets of entity objects have identical data.
   *
   * @param actuals A set of entity objects to be validated
   * @param expecteds A set of entity objects to compare against (expected values)
   */
  public void assertEntities(List<E> actuals, List<E> expecteds) {

    var actualM = mapEntitiesById(actuals);
    var expectedM = mapEntitiesById(expecteds);

    var actualJ = JacksonHelper.pretty(actualM);
    var expectedJ = JacksonHelper.pretty(expectedM);

    if (actualM.size() != expectedM.size()) {
      Assertions.fail(String.format(
          "Size of actual (%d) is not equal to expected (%d). Actual entities: %s\n  expected entities: %s",
          actualM.size(), expectedM.size(), actualJ, expectedJ));
    }

    for (var entry : expectedM.entrySet()) {
      var key = entry.getKey();
      if (!actualM.containsKey(key)) {
        Assertions.fail(String.format("no actual Object with key=%s. Actual entities: %s\n  expected entities: %s", key,
            actualJ, expectedJ));
      }

      assertEntity(entry.getValue(), actualM.get(key));
    }
  }

  /**
   * Convert a set of entity objects into a Map with id values as keys, for easy data comparison in unit tests.
   *
   * @param entities A set of entity objects
   * @return A Map with id values as keys and corresponding entity objects
   */
  public Map<String, E> mapEntitiesById(List<E> entities) {
    var r = new HashMap<String, E>();

    for (var ent : entities) {
      var map = toMap(ent);
      String identifier = (String)map.get("id");
      r.put(identifier, ent);
    }
    return r;
  }

  /**
   * 把Convert a set of entity objects into a Map.
   *
   * @param byId Whether to use id values as keys in the map
   * @param entities A set of entity objects
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> toMap(E entity) {
    var r = JacksonHelper.from(JacksonHelper.to(entity), Map.class);
    r.remove("createdAt");
    r.remove("modifiedAt");
    return r;
  }

  /**
   * Verify if two entity objects have identical data.
   * 
   * @param actual The actual entity object to be validated
   * @param expected The expected entity object to compare against
   */
  public void assertEntity(E expected, E actual) {

    var expectedM = toMap(expected);
    var actualM = toMap(actual);

    var actualId = (String)actualM.get("id");
    var expectedId = (String)expectedM.get("id");
    if (!Objects.equals(actualId, expectedId)) {
      Assertions.fail(String.format("id is not equal. \n  actual id: %s\n  expected id: %s", actualId, expectedId));
    }

    actualM.remove("id");
    expectedM.remove("id");

    if (!expectedM.equals(actualM)) {
      Assertions.fail(String.format("no same properties. \n  actual: %s\n  expected: %s\n", JacksonHelper.pretty(actual), JacksonHelper.pretty(expected)));
    }
  }

  public E importEntity(E entity) {
    return dao().save(entity);
  }

}
