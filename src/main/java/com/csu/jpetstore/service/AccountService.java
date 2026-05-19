package com.csu.jpetstore.service;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    @Autowired
    private AccountMapper accountMapper;

    public Account login(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return accountMapper.getAccountByUsernameAndPassword(account);
    }

    public Account getAccount(String username, String password) {
        return login(username, password);
    }

    @Transactional
    public void insertAccount(Account account) throws Exception {
        Account existingUser = accountMapper.getAccountByUsername(account.getUsername());
        if (existingUser != null) {
            throw new Exception("用户名已存在");
        }

        Account existingEmail = accountMapper.getAccountByEmail(account.getEmail());
        if (existingEmail != null) {
            throw new Exception("该邮箱已注册");
        }

        accountMapper.insertAccount(account);
        accountMapper.insertProfile(account);
        accountMapper.insertSignon(account);
    }

    public Account getAccountByUsername(String username) {
        if (username == null || username.isEmpty()) return null;
        return accountMapper.getAccountByUsername(username);
    }

    @Transactional
    public void updateAccount(Account account) throws Exception {
        accountMapper.updateAccount(account);
        accountMapper.updateProfile(account);
    }

    public Account getAccountByEmail(String email) {
        if (email == null || email.isEmpty()) return null;
        return accountMapper.getAccountByEmail(email);
    }

    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) throws Exception {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(oldPassword);
        Account existing = accountMapper.getAccountByUsernameAndPassword(account);
        if (existing == null) {
            throw new Exception("原密码错误");
        }
        accountMapper.updatePassword(username, newPassword);
    }
}
