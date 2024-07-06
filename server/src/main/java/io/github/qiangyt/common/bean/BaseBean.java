package io.github.qiangyt.common.bean;

import java.util.Date;

import io.github.qiangyt.common.json.JacksonHelper;

/**
 * A base bean class with a unique identifier, primarily used for DTOs/VOs
 */
@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
public class BaseBean {

    /**
     * Unique identifier
     */
    String id;

    /**
     * Creation time
     */
    Date createdAt;

    // String createdBy;
    // Date updatedAt;
    // String updatedBy;

    @Override
    public String toString() {
        return JacksonHelper.pretty(this);
    }

}
