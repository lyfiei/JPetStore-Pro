package com.csu.jpetstore.controller.api;

import com.csu.jpetstore.common.BusinessException;
import com.csu.jpetstore.common.Result;
import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.dto.ChangePasswordRequest;
import com.csu.jpetstore.dto.UpdateProfileRequest;
import com.csu.jpetstore.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/account")
public class AccountApiController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/profile")
    public Result<Map<String, Object>> getProfile(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Account account = accountService.getAccountByUsername(username);
        if (account == null) {
            throw new BusinessException("用户不存在");
        }
        return Result.success(buildUserInfo(account));
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(@Valid @RequestBody UpdateProfileRequest req,
                                      HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        Account account = accountService.getAccountByUsername(username);
        if (account == null) {
            throw new BusinessException("用户不存在");
        }

        account.setFirstName(req.getFirstName());
        account.setLastName(req.getLastName());
        account.setEmail(req.getEmail());
        account.setPhone(req.getPhone());
        account.setAddress1(req.getAddress1());
        account.setAddress2(req.getAddress2() != null ? req.getAddress2() : "");
        account.setCity(req.getCity());
        account.setState(req.getState());
        account.setZip(req.getZip());
        account.setCountry(req.getCountry());

        try {
            accountService.updateAccount(account);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }

        return Result.success();
    }

    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordRequest req,
                                       HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        try {
            accountService.changePassword(username, req.getOldPassword(), req.getNewPassword());
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        return Result.success();
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
}
