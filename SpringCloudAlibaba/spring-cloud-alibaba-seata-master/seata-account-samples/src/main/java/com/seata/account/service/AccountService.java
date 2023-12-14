package com.seata.account.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.seata.account.po.Account;

import java.math.BigDecimal;


public interface AccountService extends IService<Account> {
    void decrease(Long userId, BigDecimal money);
}
