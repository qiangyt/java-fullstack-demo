package com.example.demo.server.service;

import io.github.qiangyt.common.misc.UuidHelper;

import com.example.demo.sdk.req.CommentReq;
import com.example.demo.sdk.resp.CommentResp;
import com.example.demo.server.dao.CommentDao;
import com.example.demo.server.entity.UserEntity;
import static com.example.demo.server.entity.CommentEntity.COMMENT_MAPPER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@lombok.Setter
@lombok.Getter
public class CommentService {

    @Autowired
    CommentDao CommentDao;

    public String newComment(UserEntity creator, String postId, CommentReq req) {
        var r = COMMENT_MAPPER.map(req);
        r.setId(UuidHelper.shortUuid());
        r.setCreatedBy(creator);
        r.setPostId(postId);

        return getCommentDao().save(r).getId();
    }

    /**
     * Find all comment replies (includes replies of replies, etc.) and build the hiearchy in memory
     *
     * @param rootComments
     *
     * @return
     */
    public List<CommentResp> listComments(String postId) {
        var sorter = Comparator.comparing(CommentResp::getCreatedAt).reversed();

        // initialize the root Comment responses
        var respMap = new HashMap<String, CommentResp>();

        // step 1: find all their replies regardless of the depth
        var ents = getCommentDao().findByPostId(postId);
        var result = new ArrayList<CommentResp>(ents.size());
        ents.forEach(ent -> {
            var resp = COMMENT_MAPPER.map(ent);
            respMap.put(ent.getId(), resp);

            if (ent.getParentCommentId() == null) {
                // if parentCommentId is null, it is a root comment in the hiearchy
                result.add(resp);
            }
        });
        Collections.sort(result, sorter);

        // step 2: traverse the replies entities, build corresponding response object
        // and linked it with their parent response to build the hiearchy

        ents.forEach(ent -> {
            var pid = ent.getParentCommentId();
            if (pid != null) {
                var parent = respMap.get(pid);
                var reply = respMap.get(ent.getId());
                parent.getReplies().add(reply);
            }
        });

        // step 3: sort the replies by reply time
        respMap.values().forEach(resp -> {
            Collections.sort(resp.getReplies(), sorter);
        });

        return result;
    }

}
