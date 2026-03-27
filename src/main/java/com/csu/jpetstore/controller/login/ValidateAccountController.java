package com.csu.jpetstore.controller.login;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ValidateAccountController {

    // 保持和原来一样的写法，不报错、不改动
    private final AccountService accountService = new AccountService();

    /**
     * 验证用户名或邮箱是否已存在
     * 前端传 username 或 email
     * 返回：exist 表示已存在，ok 表示可用
     */
    @GetMapping("/validateAccount")
    @ResponseBody // 必须加！返回纯文本给AJAX
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