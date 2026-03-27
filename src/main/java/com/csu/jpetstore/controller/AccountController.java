package com.csu.jpetstore.controller;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.service.AccountService;
import com.csu.jpetstore.service.CatalogService;
import jakarta.servlet.http.HttpSession; // 注意：Spring Boot 3+ 使用 jakarta
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CatalogService catalogService;

    // 对应原来的 SignOnFormServlet (GET)
    @GetMapping("/signOnForm")
    public String signOnForm() {
        // Thymeleaf 默认去 src/main/resources/templates/ 下找文件
        // 返回 "account/signon" 会指向 templates/account/signon.html
        return "account/signon";
    }

    // 对应原来的 SignOnServlet (POST)
    @PostMapping("/signOn")
    public String signOn(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         HttpSession session,
                         Model model) {

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("signOnMsg", "用户名或密码不能为空");
            return "account/signon";
        }

        Account loginAccount = accountService.getAccount(username, password);

        if (loginAccount == null) {
            model.addAttribute("signOnMsg", "用户名或者密码错误");
            return "account/signon";
        } else {
            loginAccount.setPassword(null);
            session.setAttribute("loginAccount", loginAccount);

            if (loginAccount.isListOption()) {
                List<Product> myList = catalogService.getProductListByCategory(loginAccount.getFavouriteCategoryId());
                session.setAttribute("myList", myList);
            }
            // 重定向到主页路由
            return "redirect:/mainForm";
        }
    }


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