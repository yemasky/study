package com.seata.order.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.seata.order.po.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends BaseMapper<Order> {
    void update(Long userId, int status);

    void insertOrder(Order order);
}
