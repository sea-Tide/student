package com.student.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank(message = "账号不能为空")
    @Size(max = 50, message = "账号长度不能超过50个字符")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(max = 100, message = "密码长度不能超过100个字符")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Size(max = 100, message = "确认密码长度不能超过100个字符")
    private String confirmPassword;

    @NotBlank(message = "显示名称不能为空")
    @Size(max = 80, message = "显示名称长度不能超过80个字符")
    private String displayName;
}
