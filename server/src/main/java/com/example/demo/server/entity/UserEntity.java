package com.example.demo.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import io.github.qiangyt.common.bean.BaseEntity;
import com.example.demo.sdk.resp.UserResp;
import com.example.demo.sdk.req.SignUpReq;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
@Entity
@Table(name = "demo_user")
public class UserEntity extends BaseEntity {

    @Mapper
    public static interface UserMapper {

        UserEntity map(SignUpReq req);

        UserResp map(UserEntity entity);
    }

    public static final UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    @Column
    String name;

    @Column
    String email;

    @Column
    String password;

}
