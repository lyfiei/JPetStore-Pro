package com.csu.jpetstore.controller.login;

import com.csu.jpetstore.service.AccountService;
import com.csu.jpetstore.service.EmailService;
import com.csu.jpetstore.util.CodeUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendEmailCodeController {

    private final AccountService accountService = new AccountService();

    @PostMapping("/sendEmailCode")
    public String sendEmailCode(
            @RequestParam String email,
            @RequestParam(required = false) String isAjax,
            HttpSession session
    ) {
        // 邮箱为空
        if (email == null || email.isEmpty()) {
            return "邮箱不能为空";
        }

        // 检查邮箱是否注册
        if (accountService.getAccountByEmail(email) == null) {
            return "该邮箱未注册，请先注册账号";
        }

        try {
            // 生成6位验证码
            String code = CodeUtil.generateCode(6);

            // 发送邮件
            new EmailService().sendEmail(email, code);

            // 存入session
            session.setAttribute("emailCode", code);
            session.setAttribute("emailAccount", email);
            session.setMaxInactiveInterval(300); // 5分钟

            return "success";

        } catch (Exception e) {
            return "邮件发送失败：" + e.getMessage();
        }
    }
}