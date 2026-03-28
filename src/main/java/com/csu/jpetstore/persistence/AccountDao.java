package com.csu.jpetstore.persistence;

import com.csu.jpetstore.domain.Account;
import org.apache.ibatis.annotations.Param;

public interface AccountDao {
    Account getAccountByUsername(@Param("username") String username);

    Account getAccountByUsernameAndPassword(Account account);

    void insertAccount(Account account);

    void insertProfile(Account account);

    void insertSignon(Account account);

    void updateAccount(Account account);

    void updateProfile(Account account);

    void updateSignon(Account account);

    Account getAccountByEmail(@Param("email") String email);

}

