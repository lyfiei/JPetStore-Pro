package com.csu.jpetstore.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignOnCodeFormController {

    // 打开验证码登录页面（GET）
    @GetMapping("/signOnCodeForm")
    public String showCodeLoginForm(Model model) {
        // 给页面传默认空消息
        model.addAttribute("signOnMsg", "");
        model.addAttribute("emailPrefill", "");

        // 返回 templates/account/signon_code.html
        return "account/signon_code";
    }

    // 兼容 POST 请求（和原来Servlet一样，POST也进入页面）
    @PostMapping("/signOnCodeForm")
    public String showCodeLoginFormPost(Model model) {
        return showCodeLoginForm(model);
    }
}