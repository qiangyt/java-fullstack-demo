package com.example.demo.server.service;

import io.github.qiangyt.common.misc.UuidHelper;

import com.example.demo.sdk.req.CommentReq;
import com.example.demo.sdk.req.PostReq;
import com.example.demo.sdk.resp.MessageResp;
import com.example.demo.server.dao.MessageDao;
import com.example.demo.server.entity.MessageEntity;
import com.example.demo.server.entity.UserEntity;
import static com.example.demo.server.entity.MessageEntity.MAPPER;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@lombok.Setter
@lombok.Getter
public class MessageService {

    @Autowired
    MessageDao dao;

    public void ensurePostExists(String id) {
        getDao().get(true, id);
        //TODO: ensure it is a post instead of a comment
    }

    public String newComment(UserEntity creator, String postId, CommentReq req) {
        var r = MAPPER.map(req);
        r.setId(UuidHelper.shortUuid());
        r.setCreatedBy(creator);
        r.setPostId(postId);

        return getDao().save(r).getId();
    }

    public String newPost(UserEntity creator, PostReq req) {
        var r = MAPPER.map(req);
        r.setId(UuidHelper.shortUuid());
        r.setCreatedBy(creator);

        return getDao().save(r).getId();
    }
    
    // Instead of this approach which is composed of 2 steps (find all root
    // messages, then find all their replies), another approach is to use a single
    // step which one-time finds all messages and then build the hiearchy in memory.
    // The alternative approach is more efficient but not fits well for real-world
    // applications which definitely needs pagination at the root levels of
    // messages.
    // So considering we will need to support pagination, we take this 2-step
    // approach for now.
    public List<MessageResp> listAllPosts() {
        // step 1: find all root messages
        var postEntities = getDao().findByPostIdIsNull();

        // step 2: find all their replies (includes replies of replies, etc.) and build
        // the hiearchy in memory
        return listReplies(postEntities);
    }

    /**
     * Find all root message replies (includes replies of replies, etc.) and build the hiearchy in memory
     *
     * @param postEntities
     *
     * @return
     */
    List<MessageResp> listReplies(Collection<MessageEntity> postEntities) {
        // initialize the root message responses
        var respMap = new HashMap<String, MessageResp>();
        postEntities.forEach(ent -> respMap.put(ent.getId(), MAPPER.map(ent)));

        // step 1: find all their replies
        var postIds = postEntities.stream().map(m -> m.getId()).toList();
        var repliesEntities = getDao().findByPostIdIn(postIds);
        repliesEntities.forEach(ent -> respMap.put(ent.getId(), MAPPER.map(ent)));

        // step 2: traverse the replies entities, build corresponding response object
        // and linked it
        // with their parent response to build the hiearchy
        repliesEntities.forEach(ent -> {
            var parentId = ent.getParentCommentId();
            if (parentId != null) {
                var parentResp = respMap.get(parentId);
                var replyResp = respMap.get(ent.getId());
                parentResp.getReplies().add(replyResp);
            }
        });

        // step 3: sort the replies by reply time
        respMap.values().forEach(resp -> {
            Collections.sort(resp.getReplies(), Comparator.comparing(MessageResp::getCreatedAt).reversed());
        });

        // now build the root responses as a list, keep the original order
        var r = new ArrayList<MessageResp>(postEntities.size());
        postEntities.forEach(ent -> r.add(respMap.get(ent.getId())));
        return r;
    }

}
