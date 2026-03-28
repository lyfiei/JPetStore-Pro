package com.csu.jpetstore.persistence;

import com.csu.jpetstore.domain.Account;
import org.apache.ibatis.annotations.Mapper;

@Mapper  // 只加这一个注解！！！
public interface AccountDao {

    // 方法**一个都不用改**，原样保留！！！
    Account getAccountByUsername(String username);
    Account getAccountByUsernameAndPassword(Account account);
    void insertAccount(Account account);
    void insertProfile(Account account);
    void insertSignon(Account account);
    void updateAccount(Account account);
    void updateProfile(Account account);
    void updateSignon(Account account);
    Account getAccountByEmail(String email);

}