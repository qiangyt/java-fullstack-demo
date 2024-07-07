package com.example.demo.server.service;

import io.github.qiangyt.common.misc.UuidHelper;

import com.example.demo.sdk.req.PostReq;
import com.example.demo.sdk.resp.PostResp;
import com.example.demo.server.dao.PostDao;
import com.example.demo.server.entity.UserEntity;
import static com.example.demo.server.entity.PostEntity.POST_MAPPER;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@lombok.Setter
@lombok.Getter
public class PostService {

    @Autowired
    PostDao PostDao;

    public void ensurePostExists(String id) {
        getPostDao().get(true, id);
    }

    public String newPost(UserEntity creator, PostReq req) {
        var r = POST_MAPPER.map(req);
        r.setId(UuidHelper.shortUuid());
        r.setCreatedBy(creator);

        return getPostDao().save(r).getId();
    }

    public List<PostResp> listAllPosts() {
        var ents = getPostDao().findAllOrderByCreatedAtDesc();
        return ents.stream().map(POST_MAPPER::map).toList();
    }

}
