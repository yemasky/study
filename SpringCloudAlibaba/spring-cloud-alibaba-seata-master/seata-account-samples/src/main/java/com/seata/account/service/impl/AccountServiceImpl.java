package com.seata.account.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seata.account.dao.AccountMapper;
import com.seata.account.po.Account;
import com.seata.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * TODO
 *
 * @author zxx
 * @version 1.0
 * @date 2021/5/27 11:57
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);
    @Override
    public void decrease(Long userId, BigDecimal money) {
        LOGGER.info("------->seata-account-samples中扣减账户余额开始-------<");
        //模拟超时异常，全局事务回滚
        try {
            Thread.sleep(30*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        accountMapper.decrease(userId,money);
        LOGGER.info("------->seata-account-samples中扣减账户余额结束-------<");
    }
}
