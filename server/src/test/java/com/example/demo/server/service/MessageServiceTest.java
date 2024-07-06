package com.example.demo.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.demo.sdk.req.MessageReq;
import com.example.demo.server.dao.MessageDao;
import com.example.demo.server.entity.MessageEntity;
import com.example.demo.server.entity.UserEntity;

import io.github.qiangyt.common.security.AuthService;
import io.github.qiangyt.common.security.AuthUser;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    
    @Mock
    MessageDao messageDao;

    @Mock
    AuthService authService;

    @InjectMocks
    MessageService messageService;

    @Test
    void test_postMessage_root() {
        var req = new MessageReq();
        req.setContent("content");

        var createdBy = new UserEntity();
        createdBy.setId("u-1");

        when(authService.currentUser()).thenReturn(AuthUser.simple("u-1", "password", createdBy));
        
        var resultedMessage = new MessageEntity();
        resultedMessage.setId("0000");
        when(messageDao.save(any())).thenReturn(resultedMessage);

        assertEquals("0000", messageService.postMessage(req));

        var captor = ArgumentCaptor.forClass(MessageEntity.class);
        verify(messageDao).save(captor.capture());

        var savedMessage = captor.getValue();
        assertNotNull(savedMessage.getId());
        assertEquals("content", savedMessage.getContent());
        assertNull(savedMessage.getRootId());
        assertNull(savedMessage.getParentId());
        assertSame(createdBy, savedMessage.getCreatedBy());
    }

    
    @Test
    void test_postMessage_reply_level_1() {
        var req = new MessageReq();
        req.setParentId("p-1");
        
        var resultedMessage = new MessageEntity();
        resultedMessage.setId("0000");
        when(messageDao.save(any())).thenReturn(resultedMessage);

        // parent is a root message which has no rootId and parentId
        var parent = new MessageEntity();
        parent.setId("p-1");
        when(messageDao.get(true, "p-1")).thenReturn(parent);

        var createdBy = new UserEntity();
        when(authService.currentUser()).thenReturn(AuthUser.simple("u-1", "password", createdBy));

        messageService.postMessage(req);

        var captor = ArgumentCaptor.forClass(MessageEntity.class);
        verify(messageDao).save(captor.capture());

        var savedMessage = captor.getValue();
        assertEquals("p-1", savedMessage.getRootId());
        assertEquals("p-1", savedMessage.getParentId());
    }

    
    @Test
    void test_postMessage_reply_level_3() {
        var req = new MessageReq();
        req.setParentId("p-3");
        
        var resultedMessage = new MessageEntity();
        resultedMessage.setId("0000");
        when(messageDao.save(any())).thenReturn(resultedMessage);

        var p3 = new MessageEntity();
        p3.setId("p-3");
        p3.setRootId("p-1");
        p3.setParentId("p-2");
        when(messageDao.get(true, "p-3")).thenReturn(p3);

        var createdBy = new UserEntity();
        when(authService.currentUser()).thenReturn(AuthUser.simple("u-1", "password", createdBy));

        messageService.postMessage(req);

        var captor = ArgumentCaptor.forClass(MessageEntity.class);
        verify(messageDao).save(captor.capture());

        var savedMessage = captor.getValue();
        assertEquals("p-1", savedMessage.getRootId());
        assertEquals("p-3", savedMessage.getParentId());
    }

    /*
     *  ├── root1
     *  ├── root2
     *  │   ├── reply21
     *  │   └── reply22
     *  │       ├── reply221
     *  │       └── reply222
     *  └── root3
     *      ├── reply31
     *      └── reply32
     */
    @Test
    void testListAllMessages() {
        var milli = System.currentTimeMillis();

        var expectedRoot1 = MessageEntity.builder().id("root1").createdAt(new Date(milli--)).build();

        var expectedRoot2 = MessageEntity.builder().id("root2").createdAt(new Date(milli--)).build();
        var expectedReply21 = MessageEntity.builder().id("reply21").createdAt(new Date(milli--)).rootId("root2").parentId("root2").build();
        var expectedReply22 = MessageEntity.builder().id("reply22").createdAt(new Date(milli--)).rootId("root2").parentId("root2").build();
        var expectedReply221 = MessageEntity.builder().id("reply221").createdAt(new Date(milli--)).rootId("root2").parentId("reply22").build();
        var expectedReply222 = MessageEntity.builder().id("reply222").createdAt(new Date(milli--)).rootId("root2").parentId("reply22").build();

        var expectedRoot3 = MessageEntity.builder().id("root3").createdAt(new Date(milli--)).build();
        var expectedReply31 = MessageEntity.builder().id("reply31").createdAt(new Date(milli--)).rootId("root3").parentId("root3").build();
        var expectedReply32 = MessageEntity.builder().id("reply32").createdAt(new Date(milli--)).rootId("root3").parentId("root3").build();

        when(messageDao.findByRootIdIsNullOrderByCreatedAtDesc()).thenReturn(List.of(expectedRoot1, expectedRoot2, expectedRoot3));
        when(messageDao.findByRootIdInOrderByCreatedAtDesc(List.of("root1", "root2", "root3"))).thenReturn(List.of(expectedReply21, expectedReply22, expectedReply221, expectedReply222, expectedReply31, expectedReply32));

        var r = messageService.listAllMessages();
        assertEquals(3, r.size());

        var actualRoot1 = r.get(0);
        assertEquals(expectedRoot1.getId(), actualRoot1.getId());
        assertEquals(0, actualRoot1.getReplies().size());

        var actualRoot2 = r.get(1);
        assertEquals(expectedRoot2.getId(), actualRoot2.getId());
        assertEquals(2, actualRoot2.getReplies().size());
        
        var actualReply21 = actualRoot2.getReplies().get(0);
        assertEquals(expectedReply21.getId(), actualReply21.getId());
        assertEquals(0, actualReply21.getReplies().size());

        var actualReply22 = actualRoot2.getReplies().get(1);
        assertEquals(expectedReply22.getId(), actualReply22.getId());
        assertEquals(2, actualReply22.getReplies().size());
        
        var actualReply221 = actualReply22.getReplies().get(0);
        assertEquals(expectedReply221.getId(), actualReply221.getId());
        assertEquals(0, actualReply221.getReplies().size());

        var actualReply222 = actualReply22.getReplies().get(1);
        assertEquals(expectedReply222.getId(), actualReply222.getId());
        assertEquals(0, actualReply222.getReplies().size());

        var actualRoot3 = r.get(2);
        assertEquals(expectedRoot3.getId(), actualRoot3.getId());
        assertEquals(2, actualRoot3.getReplies().size());

        var actualReply31 = actualRoot3.getReplies().get(0);
        assertEquals(expectedReply31.getId(), actualReply31.getId());
        assertEquals(0, actualReply31.getReplies().size());

        var actualReply32 = actualRoot3.getReplies().get(1);
        assertEquals(expectedReply32.getId(), actualReply32.getId());
        assertEquals(0, actualReply32.getReplies().size());

    }
}

