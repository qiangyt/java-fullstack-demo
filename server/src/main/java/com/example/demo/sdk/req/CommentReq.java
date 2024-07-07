package com.example.demo.sdk.req;

import io.github.qiangyt.common.json.JacksonHelper;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
@lombok.EqualsAndHashCode(callSuper = false)
public class CommentReq {

    @NotEmpty(message = "内容不能为空")
    @Size(min = 3, max = 200, message = "内容长度在3~200字之间")
    String content;

    // null if is a new root message
    String parentCommentId;

    @Override
    public String toString() {
        return JacksonHelper.pretty(this);
    }

}
