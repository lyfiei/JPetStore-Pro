package com.csu.jpetstore.controller.login;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.service.AccountService;
import com.csu.jpetstore.service.CatalogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class EmailLoginController {

    // 【关键】这里不用注入，保持和你原来一样的写法，避免报错
    private final AccountService accountService = new AccountService();
    private final CatalogService catalogService = new CatalogService();

    /**
     * 邮箱验证码登录（Ajax接口）
     */
    @PostMapping("/emailLogin")
    @ResponseBody // 必须加！返回纯文本给前端AJAX
    public String emailLogin(
            @RequestParam String email,
            @RequestParam String code,
            HttpSession session
    ) {
        // 1. 从session取出保存的验证码和邮箱
        String savedCode = (String) session.getAttribute("emailCode");
        String savedEmail = (String) session.getAttribute("emailAccount");

        // 2. 校验是否过期
        if (savedCode == null || savedEmail == null) {
            return "验证码已过期，请重新获取";
        }

        // 3. 校验验证码是否匹配
        if (!savedCode.equals(code) || !savedEmail.equals(email)) {
            return "验证码错误";
        }

        // 4. 查询邮箱是否注册
        Account account = accountService.getAccountByEmail(email);
        if (account == null) {
            return "该邮箱未注册，请先注册账号";
        }

        // 5. 登录成功，存入session
        session.setAttribute("loginAccount", account);

        // 6. 收藏商品列表（和原逻辑完全一样）
        if (account.isListOption()) {
            List<Product> myList = catalogService.getProductListByCategory(account.getFavouriteCategoryId());
            session.setAttribute("myList", myList);
        }

        // 7. 返回success给前端AJAX
        return "success";
    }
}