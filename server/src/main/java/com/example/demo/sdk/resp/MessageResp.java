package com.example.demo.sdk.resp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.qiangyt.common.bean.BaseBean;
import lombok.Builder;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
public class MessageResp extends BaseBean {

    String content;

    String createdBy;

    Date createdAt;

    @Builder.Default
    List<MessageResp> replies = new ArrayList<>();

}
