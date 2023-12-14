package com.seata.account.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seata.account.po.Account;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AccountMapper extends BaseMapper<Account> {

    void decrease(Long userId, BigDecimal money);
}
