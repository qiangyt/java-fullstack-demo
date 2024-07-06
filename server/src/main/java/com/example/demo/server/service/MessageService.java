package com.example.demo.server.service;

import io.github.qiangyt.common.misc.UuidHelper;
import io.github.qiangyt.common.security.AuthService;
import io.github.qiangyt.common.security.AuthUser;

import com.example.demo.sdk.req.MessageReq;
import com.example.demo.sdk.resp.MessageResp;
import com.example.demo.server.dao.MessageDao;
import com.example.demo.server.entity.MessageEntity;
import com.example.demo.server.entity.UserEntity;
import static com.example.demo.server.entity.MessageEntity.MESSAGE_MAPPER;

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
    MessageDao messageDao;

    @Autowired
    AuthService authService;

    public String postMessage(MessageReq req) {

        String rootId;
        var parentId = req.getParentId();
        if (parentId != null) {
            // this is a reply message so rootId is not null
            var parent = getMessageDao().get(true, parentId);
            if (parent.getRootId() == null) {
                // parent is a root message which has no rootId
                rootId = parentId;
            } else {
                // parent is a reply which has a rootId
                rootId = parent.getRootId();
            }
        } else {
            // this is a root message so both rootId and parentId are null
            rootId = null;
        }

        AuthUser<UserEntity> authUser = getAuthService().currentUser();
        var createdBy = authUser.getExtra();

        var r = MESSAGE_MAPPER.map(req);
        r.setId(UuidHelper.shortUuid());
        r.setCreatedBy(createdBy);
        r.setRootId(rootId);

        return getMessageDao().save(r).getId();
    }

    // Instead of this approach which is composed of 2 steps (find all root
    // messages, then find all their replies), another approach is to use a single
    // step which one-time finds all messages and then build the hiearchy in memory.
    // The alternative approach is more efficient but not fits well for real-world
    // applications which definitely needs pagination at the root levels of
    // messages.
    // So considering we will need to support pagination, we take this 2-step
    // approach for now.
    public List<MessageResp> listAllMessages() {
        // step 1: find all root messages
        var roots = getMessageDao().findByRootIdIsNullOrderByCreatedAtDesc();

        // step 2: find all their replies (includes replies of replies, etc.) and build
        // the hiearchy in memory
        return listReplies(roots);
    }

    /**
     * Find all root message replies (includes replies of replies, etc.) and build the hiearchy in memory
     *
     * @param rootMessages
     *
     * @return
     */
    List<MessageResp> listReplies(Collection<MessageEntity> rootMessages) {
        // initialize the root message responses
        var respMap = new HashMap<String, MessageResp>();
        rootMessages.forEach(entity -> respMap.put(entity.getId(), MESSAGE_MAPPER.map(entity)));

        // step 1: find all their replies
        var rootIds = rootMessages.stream().map(m -> m.getId()).toList();
        var repliesEntities = getMessageDao().findByRootIdInOrderByCreatedAtDesc(rootIds);
        repliesEntities.forEach(entity -> respMap.put(entity.getId(), MESSAGE_MAPPER.map(entity)));

        // step 2: traverse the replies entities, build corresponding response object
        // and linked it
        // with their parent response to build the hiearchy
        repliesEntities.forEach(entity -> {
            var parentId = entity.getParentId();
            if (parentId != null) {
                var parentResp = respMap.get(parentId);
                var replyResp = respMap.get(entity.getId());
                parentResp.getReplies().add(replyResp);
            }
        });

        // step 3: sort the replies by reply time
        respMap.values().forEach(resp -> {
            Collections.sort(resp.getReplies(), Comparator.comparing(MessageResp::getCreatedAt).reversed());
        });

        // now build the root responses as a list, keep the original order
        var r = new ArrayList<MessageResp>(rootMessages.size());
        rootMessages.forEach(entity -> r.add(respMap.get(entity.getId())));
        return r;
    }

}
