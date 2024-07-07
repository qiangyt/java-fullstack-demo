package com.example.demo.server.dao;

import jakarta.annotation.Nullable;
import io.github.qiangyt.common.jpa.JpaDao;
import com.example.demo.server.entity.PostEntity;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Nullable
public interface PostDao extends JpaDao<PostEntity, String> {

    @Query("SELECT e FROM PostEntity e ORDER BY e.createdAt DESC")
    List<PostEntity> findAllOrderByCreatedAtDesc();

}
