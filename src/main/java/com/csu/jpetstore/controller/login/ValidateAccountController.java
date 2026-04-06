package com.csu.jpetstore.controller.login;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ValidateAccountController {

    // ================= 改为 Spring 注入 =================
    @Autowired
    private AccountService accountService;

    @GetMapping("/validateAccount")
    @ResponseBody
    public String validateAccount(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email
    ) {
        if (username != null) {
            Account account = accountService.getAccountByUsername(username);
            return account != null ? "exist" : "ok";
        }

        if (email != null) {
            Account account = accountService.getAccountByEmail(email);
            return account != null ? "exist" : "ok";
        }

        return "ok";
    }
}