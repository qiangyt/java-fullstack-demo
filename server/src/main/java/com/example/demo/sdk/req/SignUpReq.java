package com.example.demo.sdk.req;

import io.github.qiangyt.common.json.JacksonHelper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@lombok.Getter
@lombok.Setter
@lombok.NoArgsConstructor
@lombok.experimental.SuperBuilder
@lombok.EqualsAndHashCode(callSuper = false)
public class SignUpReq {

    @NotEmpty(message = "用户名不能为空")
    @Size(min = 5, max = 20, message = "用户名长度必须在5到20个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "用户名只能包含字母和数字")
    String name;

    @NotEmpty(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度必须在8到20个字符之间")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message = "密码必须包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符")
    String password;

    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    String email;

    @Override
    public String toString() {
        return JacksonHelper.pretty(this);
    }

}
