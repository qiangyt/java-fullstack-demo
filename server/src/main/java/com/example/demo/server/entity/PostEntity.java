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

import com.example.demo.sdk.resp.PostResp;
import com.example.demo.sdk.req.PostReq;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
@Entity
@Table(name = "demo_post")
public class PostEntity extends BaseEntity {

    @Mapper
    public static interface PostMapper {

        PostEntity map(PostReq req);

        @Mapping(target = "createdBy", source = "entity.createdBy.name")
        PostResp map(PostEntity entity);
    }

    public static final PostMapper POST_MAPPER = Mappers.getMapper(PostMapper.class);

    @Column
    String content;

    // @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    UserEntity createdBy;

}
