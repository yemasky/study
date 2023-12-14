package com.seata.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seata.order.dao.OrderMapper;
import com.seata.order.feign.AccountFeignClient;
import com.seata.order.feign.StorageFeignClient;
import com.seata.order.po.Order;
import com.seata.order.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author zxx
 * @version 1.0
 * @date 2021/5/28 11:35
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private AccountFeignClient accountFeignClient;

    @Autowired
    private StorageFeignClient storageFeignClient;

    @Autowired
    private OrderMapper orderMapper;


    /**
     * 创建订单->调用库存服务扣减库存->调用账户服务扣减账户余额->修改订单状态
     */
    @Override
    //开启分布式事务
    @GlobalTransactional
    public void create(Order order) {
        LOGGER.info("------->下单开始");
        //本应用创建订单
        orderMapper.insertOrder(order);

        //远程调用库存服务扣减库存
        LOGGER.info("------->seata-order-samples中扣减库存开始-------<");
        storageFeignClient.decrease(order.getProductId(), order.getCount());
        LOGGER.info("------->seata-order-samples中扣减库存结束-------<");

        //远程调用账户服务扣减余额
        LOGGER.info("------->seata-order-samples中扣减余额开始-------<");
        accountFeignClient.decrease(order.getUserId(), order.getMoney());
        LOGGER.info("------->seata-order-samples中扣减余额结束-------<");

        //修改订单状态为已完成
        LOGGER.info("------->seata-order-samples中修改订单状态开始-------<");
        orderMapper.update(order.getUserId(), 0);
        LOGGER.info("------->seata-order-samples中修改订单状态结束-------<");

        LOGGER.info("------->下单结束");
    }
}
