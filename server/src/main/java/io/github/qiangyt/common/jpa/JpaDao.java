package io.github.qiangyt.common.jpa;

import java.util.List;

import jakarta.annotation.Nullable;

import org.springframework.data.repository.NoRepositoryBean;

import io.github.qiangyt.common.err.NotFound;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Base DAO interface for dynamic JPA queries.
 *
 * @param T
 *            entity class type
 * @param ID
 *            id type of entity class
 */
@NoRepositoryBean
@Nullable
public interface JpaDao<T, K> extends JpaRepository<T, K> {

    // TODO: Add several commonly used default methods.
    List<T> findAll();

    default T get(boolean ensureExists, K id) {
        var r = findById(id);
        if (r.isPresent()) {
            return r.get();
        }
        if (ensureExists) {
            throw new NotFound(NotFound.Code.ENTITY_NOT_FOUND, "id=%s", String.valueOf(id));
        }
        return null;
    }

}
