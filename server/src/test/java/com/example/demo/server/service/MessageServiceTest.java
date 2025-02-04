package com.example.demo.server.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.server.dao.MessageDao;
import com.example.demo.server.entity.MessageEntity;
import com.example.demo.server.entity.UserEntity;
import com.example.demo.sdk.req.CommentReq;
import com.example.demo.sdk.req.PostReq;
import com.example.demo.sdk.resp.MessageResp;

import io.github.qiangyt.common.misc.IdGenerator;

public class MessageServiceTest {

    @Mock
    private MessageDao dao;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEnsurePostExists() {
        String postId = "post123";
        MessageEntity postEntity = new MessageEntity();
        postEntity.setId(postId);

        when(dao.get(true, postId)).thenReturn(postEntity);

        assertDoesNotThrow(() -> messageService.ensurePostExists(postId));
        verify(dao, times(1)).get(true, postId);
    }

    @Test
    public void testNewComment() {
        UserEntity creator = new UserEntity();
        creator.setName("user1");

        CommentReq req = new CommentReq();
        req.setContent("Test comment");
        req.setParentId("parent123");

        MessageEntity parentEntity = new MessageEntity();
        parentEntity.setId("parent123");
        parentEntity.setPostId("post123");

        MessageEntity commentEntity = new MessageEntity();
        commentEntity.setId("comment123");
        commentEntity.setContent(req.getContent());
        commentEntity.setParentId(req.getParentId());
        commentEntity.setPostId(parentEntity.getPostId());
        commentEntity.setCreatedBy(creator);

        when(dao.get(true, req.getParentId())).thenReturn(parentEntity);
        when(idGenerator.newId()).thenReturn("comment123");
        when(dao.save(any(MessageEntity.class))).thenReturn(commentEntity);

        MessageResp result = messageService.newComment(creator, req);

        assertNotNull(result);
        assertEquals("comment123", result.getId());
        assertEquals("Test comment", result.getContent());
        assertEquals("parent123", result.getParentId());
        assertEquals("post123", result.getPostId());
        assertEquals("user1", result.getCreatedBy());

        verify(dao, times(1)).get(true, req.getParentId());
        verify(idGenerator, times(1)).newId();
        verify(dao, times(1)).save(any(MessageEntity.class));
    }

    @Test
    public void testNewPost() {
        UserEntity creator = new UserEntity();
        creator.setName("user1");

        PostReq req = new PostReq();
        req.setContent("Test post");

        MessageEntity postEntity = new MessageEntity();
        postEntity.setId("post123");
        postEntity.setContent(req.getContent());
        postEntity.setPostId("post123");
        postEntity.setCreatedBy(creator);

        when(idGenerator.newId()).thenReturn("post123");
        when(dao.save(any(MessageEntity.class))).thenReturn(postEntity);

        MessageResp result = messageService.newPost(creator, req);

        assertNotNull(result);
        assertEquals("post123", result.getId());
        assertEquals("Test post", result.getContent());
        assertEquals("post123", result.getPostId());
        assertEquals("user1", result.getCreatedBy());
        assertNull(result.getParentId());

        verify(idGenerator, times(1)).newId();
        verify(dao, times(1)).save(any(MessageEntity.class));
    }

    @Test
    public void testListAllPosts() {
        MessageEntity post1 = new MessageEntity();
        post1.setId("post1");
        post1.setContent("Post 1");
        post1.setPostId("post1");
        post1.setCreatedBy(new UserEntity());

        MessageEntity comment1 = new MessageEntity();
        comment1.setId("comment1");
        comment1.setContent("Comment 1");
        comment1.setPostId("post1");
        comment1.setParentId("post1");
        comment1.setCreatedBy(new UserEntity());

        MessageEntity comment2 = new MessageEntity();
        comment2.setId("comment2");
        comment2.setContent("Comment 2");
        comment2.setPostId("post1");
        comment2.setParentId("comment1");
        comment2.setCreatedBy(new UserEntity());

        when(dao.findAll()).thenReturn(Arrays.asList(post1, comment1, comment2));

        List<MessageResp> result = messageService.listAllPosts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("post1", result.get(0).getId());
        assertEquals(1, result.get(0).getReplies().size());
        assertEquals("comment1", result.get(0).getReplies().get(0).getId());
        assertEquals(1, result.get(0).getReplies().get(0).getReplies().size());
        assertEquals("comment2", result.get(0).getReplies().get(0).getReplies().get(0).getId());

        verify(dao, times(1)).findAll();
    }
}