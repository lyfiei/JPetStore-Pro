package com.csu.jpetstore.service;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.persistence.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    // ====================== 核心修改 ======================
    @Autowired  // MyBatis 自动注入，不需要 new 实现类
    private AccountDao accountDao;

    // 删掉原来的构造方法！！！
    // public AccountService() {
    //     this.accountDao = new AccountDaoImpl();
    // }

    // 登录方法（完全不用改，原样保留）
    public Account getAccount(String username, String password) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        return accountDao.getAccountByUsernameAndPassword(account);
    }

    // ====================== 事务交给 Spring 管理 ======================
    @Transactional  // 自动事务，不需要手动写 JDBC 事务
    public void insertAccount(Account account) throws Exception {
        System.out.println("开始注册用户: " + account.getUsername());

        // 检查用户名
        Account existingUser = accountDao.getAccountByUsername(account.getUsername());
        if (existingUser != null) {
            throw new Exception("用户名已存在");
        }

        // 检查邮箱
        Account existingEmail = accountDao.getAccountByEmail(account.getEmail());
        if (existingEmail != null) {
            throw new Exception("该邮箱已注册");
        }

        // 直接调用，MyBatis 自动管理连接
        accountDao.insertAccount(account);
        accountDao.insertProfile(account);
        accountDao.insertSignon(account);

        System.out.println("用户注册成功: " + account.getUsername());
    }

    // 根据用户名查账户（不用改）
    public Account getAccountByUsername(String username) {
        if(username == null || username.isEmpty()) return null;
        return accountDao.getAccountByUsername(username);
    }

    // ====================== 事务交给 Spring 管理 ======================
    @Transactional
    public void updateAccount(Account account) throws Exception {
        accountDao.updateAccount(account);
        accountDao.updateProfile(account);
        accountDao.updateSignon(account);
    }

    // 根据邮箱查账户（不用改）
    public Account getAccountByEmail(String email) {
        System.out.println("AccountService: 检查邮箱 " + email);
        if (email == null || email.isEmpty()){
            System.out.println("邮箱检查为空");
            return null;
        }
        Account account = accountDao.getAccountByEmail(email);
        System.out.println("AccountService: 查询结果: " + account);
        return account;
    }
}