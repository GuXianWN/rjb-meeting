package com.guxian.auth.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
public class RegisterVo {
    @NotNull(message = "账号不能为空")
    @NotBlank(message = "账号不用为空串")
    @Min(value = 3,message = "账号的长度不能小于3")
    @Max(value = 30,message = "账号的长度不能大于30")
    public String username;

    @NotNull(message = "密码不能为空")
    @NotBlank(message = "密码不用为空串")
    @Min(value = 3,message = "密码的长度不能小于3")
    @Max(value = 30,message = "密码的长度不能大于30")
    public String password;

    @NotNull(message = "用户名账号不能为空")
    @NotBlank(message = "用户名不用为空串")
    @Min(value = 3,message = "用户名的长度不能小于3")
    @Max(value = 30,message = "用户名的长度不能大于30")
    public String account;
}
