package com.example.demo.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.demo.sdk.req.PostReq;
import com.example.demo.server.dao.PostDao;
import com.example.demo.server.entity.PostEntity;
import com.example.demo.server.entity.UserEntity;

import io.github.qiangyt.common.err.NotFound;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    
    @Mock
    PostDao dao;

    @InjectMocks
    PostService target;


    @Test
    void test_ensurePostExists() {
        var p = PostEntity.builder().id("0").build();
        when(dao.get(true, "0")).thenReturn(p);
        target.ensurePostExists("0");
    }

    @Test
    void test_newPost() {
        var req = new PostReq();
        req.setContent("content");

        var creator = UserEntity.builder().id("u").build();
        
        var resultedPost = new PostEntity();
        resultedPost.setId("0000");
        when(dao.save(any())).thenReturn(resultedPost);

        assertEquals("0000", target.newPost(creator, req));

        var captor = ArgumentCaptor.forClass(PostEntity.class);
        verify(dao).save(captor.capture());

        var savedPost = captor.getValue();
        assertNotNull(savedPost.getId());
        assertEquals("content", savedPost.getContent());
        assertSame(creator, savedPost.getCreatedBy());
    }

    @Test
    void testListAllPosts() {
        var u1 = UserEntity.builder().id("u1").name("n1").build();
        var createdAt1 = new Date();
        var p1 = PostEntity.builder().id("p1").content("c1").createdBy(u1).createdAt(createdAt1).build();

        var u2 = UserEntity.builder().id("u2").name("n2").build();
        var createdAt2 = new Date();
        var p2 = PostEntity.builder().id("p2").content("c2").createdBy(u2).createdAt(createdAt2).build();
        
        when(dao.findAllOrderByCreatedAtDesc()).thenReturn(List.of(p1, p2));
        
        var r = target.listAllPosts();
        assertEquals(2, r.size());

        var actual1 = r.get(0);
        assertEquals(p1.getId(), actual1.getId());
        assertEquals("c1", actual1.getContent());
        assertEquals("n1", actual1.getCreatedBy());
        //assertEquals(createdAt1, actual1.getCreatedAt());

        var actual2 = r.get(1);
        assertEquals(p2.getId(), actual2.getId());
        assertEquals("c2", actual2.getContent());
        assertEquals("n2", actual2.getCreatedBy());
        //assertEquals(createdAt1, actual2.getCreatedAt());
        
    }
}

