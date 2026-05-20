package com.csu.jpetstore.controller.api;

import com.csu.jpetstore.common.BusinessException;
import com.csu.jpetstore.common.Result;
import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.dto.*;
import com.csu.jpetstore.service.AccountService;
import com.csu.jpetstore.service.EmailService;
import com.csu.jpetstore.service.GitHubOAuthService;
import com.csu.jpetstore.util.CodeUtil;
import com.csu.jpetstore.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private GitHubOAuthService gitHubOAuthService;

    private final ConcurrentHashMap<String, CodeEntry> codeStore = new ConcurrentHashMap<>();

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Account account = accountService.login(request.getUsername(), request.getPassword());
        if (account == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }

        String token = jwtUtil.generateToken(account.getUsername());

        Map<String, Object> userInfo = buildUserInfo(account);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", userInfo);

        return Result.success(data);
    }

    @PostMapping("/login/email")
    public Result<Map<String, Object>> loginByEmail(@Valid @RequestBody EmailLoginRequest request) {
        CodeEntry entry = codeStore.get(request.getEmail());
        if (entry == null || System.currentTimeMillis() - entry.timestamp > 300_000) {
            throw new BusinessException("验证码无效或已过期");
        }
        if (!entry.code.equals(request.getCode())) {
            throw new BusinessException("验证码错误");
        }
        codeStore.remove(request.getEmail());

        Account account = accountService.getAccountByEmail(request.getEmail());
        if (account == null) {
            throw new BusinessException("该邮箱未注册");
        }

        String token = jwtUtil.generateToken(account.getUsername());

        Map<String, Object> userInfo = buildUserInfo(account);

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userInfo", userInfo);

        return Result.success(data);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword());
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setAddress1(request.getAddress1());
        account.setAddress2(request.getAddress2() != null ? request.getAddress2() : "");
        account.setCity(request.getCity());
        account.setState(request.getState());
        account.setZip(request.getZip());
        account.setCountry(request.getCountry());
        account.setLanguagePreference("zh_CN");
        account.setFavouriteCategoryId("FISH");
        account.setListOption(true);
        account.setBannerOption(true);

        try {
            accountService.insertAccount(account);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return Result.success();
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    @PostMapping("/send-code")
    public Result<Void> sendCode(@Valid @RequestBody SendCodeRequest request) {
        String code = CodeUtil.generateCode(6);
        codeStore.put(request.getEmail(), new CodeEntry(code, System.currentTimeMillis()));
        emailService.sendEmail(request.getEmail(), code);
        return Result.success();
    }

    @GetMapping("/validate")
    public Result<Map<String, Boolean>> validate(@RequestParam(required = false) String username,
                                                  @RequestParam(required = false) String email) {
        Map<String, Boolean> data = new HashMap<>();
        if (username != null) {
            Account account = accountService.getAccountByUsername(username);
            data.put("available", account == null);
        } else if (email != null) {
            Account account = accountService.getAccountByEmail(email);
            data.put("available", account == null);
        } else {
            throw new BusinessException("请提供用户名或邮箱");
        }
        return Result.success(data);
    }

    // ==================== GitHub OAuth ====================

    @GetMapping("/oauth/github/authorize")
    public void githubAuthorize(HttpServletResponse response) throws IOException {
        String url = gitHubOAuthService.getAuthorizationUrl();
        response.sendRedirect(url);
    }

    @GetMapping("/oauth/github/callback")
    public void githubCallback(@RequestParam String code,
                               HttpServletResponse response,
                               HttpSession session) throws IOException {
        try {
            String accessToken = gitHubOAuthService.getAccessToken(code);
            Map<String, Object> githubUser = gitHubOAuthService.getUserInfo(accessToken);
            Account account = gitHubOAuthService.findOrCreateAccount(githubUser);

            account.setPassword(null);
            session.setAttribute("loginAccount", account);

            response.sendRedirect("/mainForm");
        } catch (Exception e) {
            response.sendRedirect("/signOnForm?signOnMsg=GitHub login failed: " + e.getMessage());
        }
    }

    private Map<String, Object> buildUserInfo(Account account) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", account.getUsername());
        userInfo.put("firstName", account.getFirstName());
        userInfo.put("lastName", account.getLastName());
        userInfo.put("email", account.getEmail());
        userInfo.put("phone", account.getPhone());
        userInfo.put("address1", account.getAddress1());
        userInfo.put("address2", account.getAddress2());
        userInfo.put("city", account.getCity());
        userInfo.put("state", account.getState());
        userInfo.put("zip", account.getZip());
        userInfo.put("country", account.getCountry());
        return userInfo;
    }

    private static class CodeEntry {
        String code;
        long timestamp;

        CodeEntry(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }
    }
}
