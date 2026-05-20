package com.csu.jpetstore.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class SendCodeRequest {
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "请输入有效的邮箱地址")
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
