package com.csu.jpetstore.service;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.persistence.AccountDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final AccountDao accountDao;

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return accountDao.getAccountByUsernameAndPassword(account);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertAccount(Account account) throws Exception {
        System.out.println("开始注册用户：" + account.getUsername());

        Account existingUser = accountDao.getAccountByUsername(account.getUsername());
        if (existingUser != null) {
            throw new Exception("用户名已存在");
        }

        Account existingEmail = accountDao.getAccountByEmail(account.getEmail());
        if (existingEmail != null) {
            throw new Exception("该邮箱已注册");
        }

        accountDao.insertAccount(account);
        accountDao.insertProfile(account);
        accountDao.insertSignon(account);

        System.out.println("用户注册成功：" + account.getUsername());
    }

    public Account getAccountByUsername(String username) {
        if(username == null || username.isEmpty()) return null;
        return accountDao.getAccountByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateAccount(Account account) throws Exception {
        accountDao.updateAccount(account);
        accountDao.updateProfile(account);
        accountDao.updateSignon(account);
    }

    public Account getAccountByEmail(String email) {
        System.out.println("AccountService: 检查邮箱 " + email);
        if (email == null || email.isEmpty()){
            System.out.println("邮箱检查为空");
            return null;
        }
        Account account = accountDao.getAccountByEmail(email);
        System.out.println("AccountService: 查询结果：" + account);
        return account;
    }

}
