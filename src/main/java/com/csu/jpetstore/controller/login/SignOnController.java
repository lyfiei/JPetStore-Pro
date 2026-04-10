package com.csu.jpetstore.controller.login;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.service.AccountService;
import com.csu.jpetstore.service.CatalogService;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 替换原 SignOnServlet
 * 功能：处理账号密码登录的表单提交逻辑
 */
@Controller
public class SignOnController {

    // 原 Servlet 中的页面路径，替换为 Thymeleaf 模板路径
    private static final String SIGN_ON_FORM = "account/signon";

    // ✅ 核心改造：Spring 依赖注入（替代 new 关键字，符合最佳实践）
    private final AccountService accountService;
    private final CatalogService catalogService;

    // 构造器注入（IDEA 会自动提示注入，也可手动加 @Autowired，SpringBoot 2.6+ 推荐构造器注入）
    public SignOnController(AccountService accountService, CatalogService catalogService) {
        this.accountService = accountService;
        this.catalogService = catalogService;
    }

    /**
     * 对应原 Servlet 的 doPost 方法
     * @PostMapping("/signOn") ：保持和原 Servlet 一致的表单提交路径
     */
    @PostMapping("/signOn")
    public String handleSignOn(
            // ✅ 用 @RequestParam 接收表单参数（替代 req.getParameter()）
            @RequestParam String username,
            @RequestParam String password,
            // ✅ 用 Model 传递页面数据（替代 req.setAttribute()）
            Model model,
            // ✅ 注入 HttpSession（保留原有会话逻辑）
            HttpSession session) {

        // 1. 空值校验（和原逻辑一致）
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("signOnMsg", "用户名或密码不能为空");
            return SIGN_ON_FORM; // 返回登录页（替代 forward 转发）
        }

        // 2. 验证账号密码（用注入的 service，替代 new AccountService()）
        Account loginAccount = accountService.getAccount(username, password);

        if (loginAccount == null) {
            // 登录失败：返回登录页 + 错误提示
            model.addAttribute("signOnMsg", "用户名或者密码错误");
            return SIGN_ON_FORM;
        } else {
            // 3. 登录成功：处理会话数据（和原逻辑一致）
            loginAccount.setPassword(null);
            session.setAttribute("loginAccount", loginAccount);

            // 4. 处理收藏分类的商品列表（和原逻辑一致）
            if (loginAccount.isListOption()) {
                List<Product> myList = catalogService.getProductListByCategory(loginAccount.getFavouriteCategoryId());
                session.setAttribute("myList", myList);
            }

            // 5. 跳转到首页（替代 resp.sendRedirect("mainForm")）
            return "redirect:/mainForm";
        }
    }
}