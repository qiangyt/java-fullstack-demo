package com.example.demo.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.github.qiangyt.common.bean.BaseEntity;
import com.example.demo.sdk.resp.MessageResp;
import com.example.demo.sdk.req.MessageReq;

//import org.springframework.data.annotation.CreatedBy;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
@Entity
@Table(name = "demo_message")
public class MessageEntity extends BaseEntity {

    @Mapper
    public static interface MessageMapper {

        MessageEntity map(MessageReq req);

        @Mapping(target = "createdBy", source = "entity.createdBy.name")
        MessageResp map(MessageEntity entity);
    }

    public static final MessageMapper MESSAGE_MAPPER = Mappers.getMapper(MessageMapper.class);

    @Column
    String content;

    @Column(name = "root_id")
    String rootId;

    @Column(name = "parent_id")
    String parentId;

    // @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    UserEntity createdBy;

}
