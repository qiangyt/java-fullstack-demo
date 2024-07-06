package io.github.qiangyt.common.jpa;

import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.AccessLevel;

/**
 * Base DAO class for convenient QueryDSL implementation of dynamic JPA queries.
 *
 * @param T
 *            entity class type
 * @param ID
 *            id type of entity class
 */
@lombok.Getter(AccessLevel.PROTECTED)
public class AbstractJpaDao<T, K> extends SimpleJpaRepository<T, K> implements JpaDao<T, K> {

    final JPAQueryFactory queryFactory;

    /**
     * Path of QueryDSL entity class which represents a QueryDSL entity class
     */
    final EntityPathBase<T> entityPath;

    final EntityManager entityManager;

    public AbstractJpaDao(Class<T> entityClass, EntityPathBase<T> entityPath, EntityManager entityManager) {
        super(entityClass, entityManager);

        this.entityPath = entityPath;
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    // TODO: Add several commonly used QueryDSL query methods.

}
