package io.github.qiangyt.common.bean;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.github.qiangyt.common.json.JacksonHelper;

/**
 * A base entity class with a unique identifier
 *
 */
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    /**
     * Unique identifier
     */
    @Id
    @Column(name = "id")
    String id;

    /**
     * Creation time
     */
    @CreatedDate
    @Column(name = "created_at")
    Date createdAt;

    // String createdBy;
    // Date updatedAt;
    // String updatedBy;

    @Override
    public String toString() {
        return JacksonHelper.pretty(this);
    }

}
