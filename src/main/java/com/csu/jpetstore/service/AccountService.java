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

    public Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return accountMapper.getAccountByUsernameAndPassword(account);
    }

    @Transactional
    public void insertAccount(Account account) throws Exception {
        System.out.println("开始注册用户: " + account.getUsername());

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

        System.out.println("用户注册成功: " + account.getUsername());
    }

    public Account getAccountByUsername(String username) {
        if(username == null || username.isEmpty()) return null;
        return accountMapper.getAccountByUsername(username);
    }

    @Transactional
    public void updateAccount(Account account) throws Exception {
        accountMapper.updateAccount(account);
        accountMapper.updateProfile(account);
        accountMapper.updateSignon(account);
    }

    public Account getAccountByEmail(String email) {
        System.out.println("AccountService: 检查邮箱 " + email);
        if (email == null || email.isEmpty()){
            System.out.println("邮箱检查为空");
            return null;
        }
        Account account = accountMapper.getAccountByEmail(email);
        System.out.println("AccountService: 查询结果: " + account);
        return account;
    }
}