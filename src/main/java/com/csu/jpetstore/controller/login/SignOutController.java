package com.csu.jpetstore.controller.login;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 替换原 SignOutServlet
 * 功能：处理用户登出逻辑，销毁会话并跳转首页
 */
@Controller
public class SignOutController {

    /**
     * 对应原 Servlet 的 doGet 方法
     * @GetMapping("/signOut") ：保持和原 Servlet 一致的访问路径
     */
    @GetMapping("/signOut")
    public String handleSignOut(HttpSession session) {
        // 1. 销毁 Session（和原逻辑一致：不创建新 Session，存在则销毁）
        // SpringMVC 注入的 HttpSession 若未创建则为 null，无需手动调用 getSession(false)
        if (session != null) {
            session.invalidate();
        }

        // 2. 跳转到首页（并传递登出成功提示，替代原 request.setAttribute + redirect）
        // 用 redirect 带参数传递提示信息，首页可通过 Model 获取
        return "redirect:/mainForm?signOnMsg=You have successfully signed out.";
    }

    /**
     * 对应原 Servlet 的 doPost 方法
     * 兼容 POST 请求（比如前端用表单 POST 提交登出）
     */
    @PostMapping("/signOut")
    public String handleSignOutPost(HttpSession session) {
        // 复用 GET 方法的逻辑
        return handleSignOut(session);
    }
}