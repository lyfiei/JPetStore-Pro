package com.csu.jpetstore.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignOutController {

    @GetMapping("/signOut")
    public String signOut(HttpSession session, Model model) {
        // 不创建新 session，如果 session 存在则销毁
        if (session != null) {
            session.invalidate();
        }

        // 设置退出登录提示信息
        model.addAttribute("signOnMsg", "You have successfully signed out.");

        // 跳转到登录页面（使用 forward 而不是 redirect，这样可以保留 model 中的消息）
        return "forward:/signOnForm";
    }
}
