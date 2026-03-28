package com.csu.jpetstore.controller.login;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.service.AccountService;
import com.csu.jpetstore.service.CatalogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class EmailLoginController {

    // ====================== 只改这里 ======================
    @Autowired
    private AccountService accountService;

    @Autowired
    private CatalogService catalogService;

    /**
     * 邮箱验证码登录（Ajax接口）
     */
    @PostMapping("/emailLogin")
    @ResponseBody
    public String emailLogin(
            @RequestParam String email,
            @RequestParam String code,
            HttpSession session
    ) {
        String savedCode = (String) session.getAttribute("emailCode");
        String savedEmail = (String) session.getAttribute("emailAccount");

        if (savedCode == null || savedEmail == null) {
            return "验证码已过期，请重新获取";
        }

        if (!savedCode.equals(code) || !savedEmail.equals(email)) {
            return "验证码错误";
        }

        Account account = accountService.getAccountByEmail(email);
        if (account == null) {
            return "该邮箱未注册，请先注册账号";
        }

        session.setAttribute("loginAccount", account);

        if (account.isListOption()) {
            List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
            session.setAttribute("myList", myList);
        }

        return "success";
    }
}