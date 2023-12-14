package com.seata.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.seata.order.po.Order;

public interface OrderService extends IService<Order> {

    void create(Order order);
}
