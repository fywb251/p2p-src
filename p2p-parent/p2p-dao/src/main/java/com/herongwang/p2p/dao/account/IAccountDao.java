package com.herongwang.p2p.dao.account;

import com.herongwang.p2p.entity.account.AccountEntity;
import com.sxj.mybatis.orm.annotations.Insert;

public interface IAccountDao
{
    /**
     * 新增账户信息
     */
    @Insert
    public void addAccount(AccountEntity account);
}