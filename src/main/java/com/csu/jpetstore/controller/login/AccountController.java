package com.csu.jpetstore.controller.login;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.service.AccountService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 账户管理控制器 - 合并了注册、验证、资料修改等功能
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // ==================== 注册页面 ====================

    @GetMapping("/registerForm")
    public String showRegisterForm() {
        return "account/register";
    }

    // ==================== 验证账号/邮箱（AJAX） ====================

    @GetMapping("/validateAccount")
    @ResponseBody
    public String validateAccount(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email) {

        if (username != null && !username.trim().isEmpty()) {
            Account account = accountService.getAccountByUsername(username);
            return account != null ? "exist" : "ok";
        }

        if (email != null && !email.trim().isEmpty()) {
            Account account = accountService.getAccountByEmail(email);
            return account != null ? "exist" : "ok";
        }

        return "ok";
    }

    // ==================== 注册提交 ====================

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

    // ==================== 个人资料页面 ====================

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        if (loginAccount == null) {
            return "redirect:/signOnForm";
        }

        Account account = accountService.getAccountByUsername(loginAccount.getUsername());
        model.addAttribute("account", account);

        return "account/profile";
    }

    // ==================== 更新个人资料 ====================

    @PostMapping("/updateProfile")
    @ResponseBody
    public String updateProfile(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "address1", required = false) String address1,
            @RequestParam(value = "address2", required = false) String address2,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "zip", required = false) String zip,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "languagePreference", required = false) String languagePreference,
            @RequestParam(value = "favouriteCategoryId", required = false) String favouriteCategoryId,
            @RequestParam(value = "listOption", required = false) String listOption,
            @RequestParam(value = "bannerOption", required = false) String bannerOption,
            HttpSession session) {

        Account account = (Account) session.getAttribute("loginAccount");

        if (account == null) {
            return "session_expired";
        }

        String errorMsg = null;
        if (firstName == null || firstName.trim().isEmpty()) {
            errorMsg = "名字不能为空";
        } else if (lastName == null || lastName.trim().isEmpty()) {
            errorMsg = "姓氏不能为空";
        } else if (email == null || email.trim().isEmpty()) {
            errorMsg = "邮箱不能为空";
        }

        if (errorMsg == null) {
            Account existingAccount = accountService.getAccountByEmail(email);
            if (existingAccount != null && !existingAccount.getUsername().equals(account.getUsername())) {
                errorMsg = "该邮箱已被其他用户使用，请更换邮箱";
            }
        }

        if (errorMsg != null) {
            return errorMsg;
        }

        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setEmail(email);
        account.setPhone(phone);
        account.setAddress1(address1);
        account.setAddress2(address2);
        account.setCity(city);
        account.setState(state);
        account.setZip(zip);
        account.setCountry(country);
        account.setLanguagePreference(languagePreference);
        account.setFavouriteCategoryId(favouriteCategoryId);
        account.setListOption("on".equals(listOption) || "true".equals(listOption));
        account.setBannerOption("on".equals(bannerOption) || "true".equals(bannerOption));

        try {
            accountService.updateAccount(account);
            session.setAttribute("loginAccount", account);
            return "success";
        } catch (Exception e) {
            return "数据库更新失败：" + e.getMessage();
        }
    }
}
