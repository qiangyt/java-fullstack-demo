package com.example.demo.sdk.resp;

import io.github.qiangyt.common.bean.BaseBean;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
public class UserResp extends BaseBean {

    String name;

    String email;

}
