package com.example.demo.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.demo.sdk.req.CommentReq;
import com.example.demo.server.dao.CommentDao;
import com.example.demo.server.entity.CommentEntity;
import static com.example.demo.server.entity.CommentEntity.builder;
import com.example.demo.server.entity.UserEntity;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {
    
    @Mock
    CommentDao dao;

    @InjectMocks
    CommentService target;

    @Test
    void test_newComment() {
        var creator = UserEntity.builder().id("u").build();
        var req = CommentReq.builder().content("c").parentCommentId("parent").build();

        var resultedComment = new CommentEntity();
        resultedComment.setId("0000");
        when(dao.save(any())).thenReturn(resultedComment);

        assertEquals("0000", target.newComment(creator, "p", req));

        var captor = ArgumentCaptor.forClass(CommentEntity.class);
        verify(dao).save(captor.capture());

        var savedComment = captor.getValue();
        assertNotNull(savedComment.getId());
        assertEquals("c", savedComment.getContent());
        assertSame(creator, savedComment.getCreatedBy());
        assertEquals("p", savedComment.getPostId());
        assertEquals("parent", savedComment.getParentCommentId());
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
    void test_listComments() {
        var milli = System.currentTimeMillis();

        var expectedRoot1 = builder().id("root1").createdAt(new Date(milli--)).build();

        var expectedRoot2 = builder().id("root2").createdAt(new Date(milli--)).build();
        var expectedReply21 = builder().id("reply21").createdAt(new Date(milli--)).parentCommentId("root2").build();
        var expectedReply22 = builder().id("reply22").createdAt(new Date(milli--)).parentCommentId("root2").build();
        var expectedReply221 = builder().id("reply221").createdAt(new Date(milli--)).parentCommentId("reply22").build();
        var expectedReply222 = builder().id("reply222").createdAt(new Date(milli--)).parentCommentId("reply22").build();

        var expectedRoot3 = builder().id("root3").createdAt(new Date(milli--)).build();
        var expectedReply31 = builder().id("reply31").createdAt(new Date(milli--)).parentCommentId("root3").build();
        var expectedReply32 = builder().id("reply32").createdAt(new Date(milli--)).parentCommentId("root3").build();

        when(dao.findByPostId("pid")).thenReturn(List.of(expectedRoot1, expectedRoot2, expectedRoot3, 
            expectedReply21, expectedReply22, expectedReply221, expectedReply222, expectedReply31, expectedReply32));

        var r = target.listComments("pid");
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

