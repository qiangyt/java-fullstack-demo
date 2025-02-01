package com.example.demo.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.demo.sdk.req.PostReq;
import com.example.demo.sdk.req.CommentReq;
import com.example.demo.server.dao.MessageDao;
import com.example.demo.server.entity.MessageEntity;
import com.example.demo.server.entity.UserEntity;

import io.github.qiangyt.common.misc.IdGenerator;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    
    @Mock
    MessageDao dao;

    @Mock
    IdGenerator idGenerator;

    @InjectMocks
    MessageService target;
    
    @Test
    void test_ensurePostExists() {
        var p = MessageEntity.builder().id("0").build();
        when(dao.get(true, "0")).thenReturn(p);
        target.ensurePostExists("0");
    }

    @Test
    void test_newPost() {
        var req = new PostReq();
        req.setContent("content");

        var creator = UserEntity.builder().id("u").build();
        
        var resultedPost = new MessageEntity();
        resultedPost.setId("0000");
        when(dao.save(any())).thenReturn(resultedPost);
        when(idGenerator.newId()).thenReturn("0000");

        assertEquals("0000", target.newPost(creator, req).getId());

        var captor = ArgumentCaptor.forClass(MessageEntity.class);
        verify(dao).save(captor.capture());

        var savedPost = captor.getValue();
        assertEquals("0000", savedPost.getId());
        assertEquals("content", savedPost.getContent());
        assertEquals("0000", savedPost.getPostId());
        assertNull(savedPost.getParentId());
        assertSame(creator, savedPost.getCreatedBy());
    }


    @Test
    void test_newComment() {
        var creator = UserEntity.builder().id("u").build();
        var req = CommentReq.builder().content("c").parentId("parent-id").build();

        var parent = MessageEntity.builder().id("parent-id").postId("post-id").build();
        when(dao.get(true, "parent-id")).thenReturn(parent);

        var resultedComment = new MessageEntity();
        resultedComment.setId("0000");
        when(dao.save(any())).thenReturn(resultedComment);
        when(idGenerator.newId()).thenReturn("0000");

        assertEquals("0000", target.newComment(creator, req).getId());

        var captor = ArgumentCaptor.forClass(MessageEntity.class);
        verify(dao).save(captor.capture());

        var savedComment = captor.getValue();
        assertEquals("0000", savedComment.getId());
        assertEquals("c", savedComment.getContent());
        assertEquals("post-id", savedComment.getPostId());
        assertEquals("parent-id", savedComment.getParentId());
        assertSame(creator, savedComment.getCreatedBy());
    }

    /*
     *  ├── post1
     *  ├── post2
     *  │   ├── reply21
     *  │   └── reply22
     *  │       ├── reply221
     *  │       └── reply222
     *  └── post3
     *      ├── reply31
     *      └── reply32
     */
    //@Disabled
    @Test
    void test_listAllPosts() {
        var milli = System.currentTimeMillis();

        var expectedPost1 = MessageEntity.builder().id("post1").createdAt(new Date(milli--)).postId("post1").build();

        var expectedPost2 = MessageEntity.builder().id("post2").createdAt(new Date(milli--)).postId("post2").build();
        var expectedReply21 = MessageEntity.builder().id("reply21").createdAt(new Date(milli--)).postId("post2").parentId("post2").build();
        var expectedReply22 = MessageEntity.builder().id("reply22").createdAt(new Date(milli--)).postId("post2").parentId("post2").build();
        var expectedReply221 = MessageEntity.builder().id("reply221").createdAt(new Date(milli--)).postId("post2").parentId("reply22").build();
        var expectedReply222 = MessageEntity.builder().id("reply222").createdAt(new Date(milli--)).postId("post2").parentId("reply22").build();

        var expectedPost3 = MessageEntity.builder().id("post3").createdAt(new Date(milli--)).postId("post3").build();
        var expectedReply31 = MessageEntity.builder().id("reply31").createdAt(new Date(milli--)).postId("post3").parentId("post3").build();
        var expectedReply32 = MessageEntity.builder().id("reply32").createdAt(new Date(milli--)).postId("post3").parentId("post3").build();

        when(dao.findAll()).thenReturn(List.of(expectedPost1, 
                                               expectedPost2, expectedReply21, expectedReply22, expectedReply221, expectedReply222, 
                                               expectedPost3, expectedReply31, expectedReply32));

        var r = target.listAllPosts();
        assertEquals(3, r.size());

        var actualPost1 = r.get(0);
        assertEquals(expectedPost1.getId(), actualPost1.getId());
        assertEquals(0, actualPost1.getReplies().size());

        var actualPost2 = r.get(1);
        assertEquals(expectedPost2.getId(), actualPost2.getId());
        assertEquals(2, actualPost2.getReplies().size());
        
        var actualReply21 = actualPost2.getReplies().get(0);
        assertEquals(expectedReply21.getId(), actualReply21.getId());
        assertEquals(0, actualReply21.getReplies().size());

        var actualReply22 = actualPost2.getReplies().get(1);
        assertEquals(expectedReply22.getId(), actualReply22.getId());
        assertEquals(2, actualReply22.getReplies().size());
        
        var actualReply221 = actualReply22.getReplies().get(0);
        assertEquals(expectedReply221.getId(), actualReply221.getId());
        assertEquals(0, actualReply221.getReplies().size());

        var actualReply222 = actualReply22.getReplies().get(1);
        assertEquals(expectedReply222.getId(), actualReply222.getId());
        assertEquals(0, actualReply222.getReplies().size());

        var actualPost3 = r.get(2);
        assertEquals(expectedPost3.getId(), actualPost3.getId());
        assertEquals(2, actualPost3.getReplies().size());

        var actualReply31 = actualPost3.getReplies().get(0);
        assertEquals(expectedReply31.getId(), actualReply31.getId());
        assertEquals(0, actualReply31.getReplies().size());

        var actualReply32 = actualPost3.getReplies().get(1);
        assertEquals(expectedReply32.getId(), actualReply32.getId());
        assertEquals(0, actualReply32.getReplies().size());

    }
}

