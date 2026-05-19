package com.csu.jpetstore.controller;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.service.AccountService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    // 对应 RegisterFormServlet (GET)
    @GetMapping("/registerForm")
    public String registerForm() {
        return "account/register"; // 跳转到 templates/account/register.html
    }

    // 对应 RegisterServlet (POST)
    @PostMapping("/register")
    public String register(Account account,
                           String confirmPassword,
                           String captchaInput,
                           HttpSession session,
                           Model model) {

        String captchaSession = (String) session.getAttribute("captcha");
        String msg = null;

        // 逻辑校验
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            msg = "用户名不能为空";
        } else if (account.getPassword() == null || !account.getPassword().equals(confirmPassword)) {
            msg = "密码不匹配";
        } else if (captchaInput == null || !captchaInput.equalsIgnoreCase(captchaSession)) {
            msg = "验证码错误";
        }

        if (msg != null) {
            model.addAttribute("registerMsg", msg);
            return "account/register";
        }

        try {
            accountService.insertAccount(account);
            return "redirect:/signOnForm"; // 注册成功跳转登录
        } catch (Exception e) {
            model.addAttribute("registerMsg", e.getMessage());
            return "account/register";
        }
    }

    // 处理注册页面的 AJAX 用户名/邮箱 重复校验
    @GetMapping("/validateAccount")
    @ResponseBody // 关键：直接返回数据，不跳转页面
    public String validateAccount(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "email", required = false) String email) {
        if (username != null && !username.trim().isEmpty()) {
            // 假设你的 service 有这个方法，如果没有需要去 service 里写一个
            Account account = accountService.getAccountByUsername(username);
            return account != null ? "exist" : "ok";
        }
        if (email != null && !email.trim().isEmpty()) {
            // 校验邮箱同理
            Account account = accountService.getAccountByEmail(email);
            return account != null ? "exist" : "ok";
        }
        return "ok";
    }
}