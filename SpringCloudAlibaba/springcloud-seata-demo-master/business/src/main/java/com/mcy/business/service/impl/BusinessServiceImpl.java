package com.mcy.business.service.impl;

import com.mcy.business.feign.OrderClient;
import com.mcy.business.feign.ProductClient;
import com.mcy.business.feign.UserClient;
import com.mcy.business.service.BusinessService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private OrderClient orderClient;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ProductClient productClient;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public String booking(String userId, String productId, int count) {
        BigDecimal money = new BigDecimal(1000);
        log.info("下单，用户：{}，产品：{},数量：{}",userId,productId,count);
        /**下面就是调用订单服务、用户服务、产品服务*/
        orderClient.create(userId,productId,count,money);
        userClient.debit(userId,money);
        productClient.deduct(productId,count);
        return "success";
    }
}
