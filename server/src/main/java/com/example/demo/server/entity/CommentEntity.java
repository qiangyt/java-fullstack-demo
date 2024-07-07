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

import com.example.demo.sdk.resp.CommentResp;
import com.example.demo.sdk.req.CommentReq;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
@Entity
@Table(name = "demo_comment")
public class CommentEntity extends BaseEntity {

    @Mapper
    public static interface CommentMapper {

        CommentEntity map(CommentReq req);

        @Mapping(target = "createdBy", source = "entity.createdBy.name")
        CommentResp map(CommentEntity entity);
    }

    public static final CommentMapper COMMENT_MAPPER = Mappers.getMapper(CommentMapper.class);

    @Column
    String content;

    @Column(name = "post_id")
    String postId;

    // if the parentCommentId is not null, this is a comment replies to another comment, otherwise it is a comment
    // replies to the post
    @Column(name = "parent_comment_id")
    String parentCommentId;

    // @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    UserEntity createdBy;

}
