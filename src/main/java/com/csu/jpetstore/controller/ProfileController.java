package com.csu.jpetstore.controller;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProfileController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/profileForm")
    public String profileForm(HttpSession session, Model model) {
        System.out.println(">>> 正在进入 My Account 页面");
        Account loginAccount = (Account) session.getAttribute("loginAccount");

        if (loginAccount == null) {
            return "redirect:/signOnForm";
        }

        Account account = accountService.getAccountByUsername(loginAccount.getUsername());
        model.addAttribute("account", account);

        return "account/profile";
    }

    @PostMapping("/updateProfile")
    @ResponseBody
    public String updateProfile(@RequestParam(value = "firstName", required = false) String firstName,
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

