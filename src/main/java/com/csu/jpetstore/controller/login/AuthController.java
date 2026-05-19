package com.csu.jpetstore.controller.login;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.service.AccountService;
import com.csu.jpetstore.service.CatalogService;
import com.csu.jpetstore.service.EmailService;
import com.csu.jpetstore.util.CodeUtil;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 认证控制器 - 合并了登录、登出、邮箱登录等功能
 */
@Controller
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private EmailService emailService;

    // ==================== 登录页面 ====================

    @GetMapping("/signOnForm")
    public String showSignOnForm(Model model) {
        model.addAttribute("signOnMsg", "");
        return "account/signon";
    }

    // ==================== 账号密码登录 ====================

    @PostMapping("/signOn")
    public String handleSignOn(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {

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

            return "redirect:/mainForm";
        }
    }

    // ==================== 登出 ====================

    @GetMapping("/signOut")
    public String handleSignOut(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/mainForm?signOnMsg=You have successfully signed out.";
    }

    @PostMapping("/signOut")
    public String handleSignOutPost(HttpSession session) {
        return handleSignOut(session);
    }

    // ==================== 验证码登录页面 ====================

    @GetMapping("/signOnCodeForm")
    public String showSignOnCodeForm() {
        return "account/signonCode";
    }

    // ==================== 发送邮箱验证码 ====================

    @PostMapping("/sendEmailCode")
    @ResponseBody
    public String sendEmailCode(
            @RequestParam String email,
            HttpSession session) {

        if (email == null || email.isEmpty()) {
            return "邮箱不能为空";
        }

        if (accountService.getAccountByEmail(email) == null) {
            return "该邮箱未注册，请先注册账号";
        }

        try {
            String code = CodeUtil.generateCode(6);
            emailService.sendEmail(email, code);

            session.setAttribute("emailCode", code);
            session.setAttribute("emailAccount", email);
            session.setMaxInactiveInterval(300); // 5分钟

            return "success";
        } catch (Exception e) {
            return "邮件发送失败：" + e.getMessage();
        }
    }

    // ==================== 邮箱验证码登录 ====================

    @PostMapping("/emailLogin")
    public String emailLogin(
            @RequestParam String email,
            @RequestParam String code,
            Model model,
            HttpSession session) {

        String sessionCode = (String) session.getAttribute("emailCode");
        String sessionEmail = (String) session.getAttribute("emailAccount");

        if (sessionCode == null || !sessionCode.equals(code)) {
            model.addAttribute("errorMsg", "验证码错误或已过期");
            return "account/signonCode";
        }

        if (sessionEmail == null || !sessionEmail.equals(email)) {
            model.addAttribute("errorMsg", "邮箱不匹配");
            return "account/signonCode";
        }

        Account loginAccount = accountService.getAccountByEmail(email);
        if (loginAccount == null) {
            model.addAttribute("errorMsg", "用户不存在");
            return "account/signonCode";
        }

        loginAccount.setPassword(null);
        session.setAttribute("loginAccount", loginAccount);
        session.removeAttribute("emailCode");
        session.removeAttribute("emailAccount");

        if (loginAccount.isListOption()) {
            List<Product> myList = catalogService.getProductListByCategory(loginAccount.getFavouriteCategoryId());
            session.setAttribute("myList", myList);
        }

        return "redirect:/mainForm";
    }
}
