package com.example.demo.server.service;

import io.github.qiangyt.common.misc.IdGenerator;
import io.github.qiangyt.common.misc.UuidHelper;

import com.example.demo.sdk.req.CommentReq;
import com.example.demo.sdk.req.PostReq;
import com.example.demo.sdk.resp.MessageResp;
import com.example.demo.server.dao.MessageDao;
import com.example.demo.server.entity.UserEntity;
import static com.example.demo.server.entity.MessageEntity.MAPPER;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@lombok.Setter
@lombok.Getter
public class MessageService {

    @Autowired
    MessageDao dao;

    @Autowired
    IdGenerator idGenerator;

    public void ensurePostExists(String id) {
        getDao().get(true, id);
        // TODO: ensure it is a post instead of a comment
    }

    public MessageResp newComment(UserEntity creator, CommentReq req) {
        var parentId = req.getParentId();
        var parent = getDao().get(true, parentId);

        var r = MAPPER.map(req);
        r.setId(getIdGenerator().newId());
        r.setCreatedBy(creator);
        r.setPostId(parent.getPostId());
        r.setParentId(parentId);

        return MAPPER.map(getDao().save(r));
    }

    public MessageResp newPost(UserEntity creator, PostReq req) {
        var id = getIdGenerator().newId();

        var r = MAPPER.map(req);
        r.setId(id);
        r.setCreatedBy(creator);
        r.setPostId(id);
        // r.setParentId(null);

        return MAPPER.map(getDao().save(r));
    }

    /**
     * Find all posts includes replies of replies. Build the hiearchy in memory
     *
     * @return
     */
    public List<MessageResp> listAllPosts() {
        // initialize the root message responses
        var msgMap = new HashMap<String, MessageResp>();
        var posts = new LinkedList<MessageResp>();

        var ents = dao.findAll();
        ents.forEach(ent -> {
            var msg = MAPPER.map(ent);
            msgMap.put(ent.getId(), msg);
            if (ent.getParentId() == null) {
                // found a post
                posts.add(msg);
            }
        });

        // traverse to build and linked it to their parent response to build the hiearchy
        msgMap.values().forEach(msg -> {
            var parentId = msg.getParentId();
            if (parentId != null) {
                var parent = msgMap.get(parentId);
                parent.getReplies().add(msg);
            }
        });

        // step 3: sort the replies by reply time
        var sorter = Comparator.comparing(MessageResp::getCreatedAt).reversed();
        msgMap.values().forEach(msg -> {
            Collections.sort(msg.getReplies(), sorter);
        });

        // sort the posts
        Collections.sort(posts, sorter);
        return posts;
    }

}
