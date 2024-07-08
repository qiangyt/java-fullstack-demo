package com.example.demo.sdk.resp;

import java.util.ArrayList;
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

    @Builder.Default
    List<MessageResp> replies = new ArrayList<>();

}
