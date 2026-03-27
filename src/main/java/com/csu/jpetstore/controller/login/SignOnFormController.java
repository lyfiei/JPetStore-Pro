package com.csu.jpetstore.controller.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 替换原 SignOnFormServlet
 * 功能：展示账号密码登录页面（对应 signon.html）
 */
@Controller
public class SignOnFormController {

    // 原 Servlet 中定义的页面路径，替换为 Thymeleaf 模板路径（无需 WEB-INF/jsp 前缀）
    private static final String SIGN_ON_FORM = "account/signon";

    /**
     * 对应原 Servlet 的 doGet 方法
     * @GetMapping("/signOnForm") ：保持和原 Servlet 一致的访问路径，前端无需改链接
     */
    @GetMapping("/signOnForm")
    public String showSignOnForm(Model model) {
        // 1. 初始化登录错误提示（空值，登录失败时由 SignOnController 赋值）
        // 替代原 Servlet 中 req.setAttribute("signOnMsg", "") 的逻辑
        model.addAttribute("signOnMsg", "");

        // 2. 返回 Thymeleaf 模板路径（对应 templates/account/signon.html）
        // 替代原 Servlet 的 req.getRequestDispatcher(SIGN_ON_FORM).forward(req, resp)
        return SIGN_ON_FORM;
    }
}