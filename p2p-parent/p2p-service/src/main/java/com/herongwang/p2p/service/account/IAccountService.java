package com.herongwang.p2p.service.account;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.herongwang.p2p.model.users.UserModel;

public interface IAccountService
{
    /**
     * 增加用户信息
     */
    public UserModel addAccount(AccountEntity account);
}
