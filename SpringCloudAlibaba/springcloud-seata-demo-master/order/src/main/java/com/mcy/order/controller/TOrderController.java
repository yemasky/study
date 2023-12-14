package com.mcy.order.controller;


import com.mcy.order.entity.TOrder;
import com.mcy.order.service.ITOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author mcy
 * @since 2020-06-06
 */
@Slf4j
@RestController
@RequestMapping("order/")
public class TOrderController {
    @Autowired
    private ITOrderService orderService;

    @PostMapping("create")
    public String create(String userId, String productId, int count, BigDecimal amount){
        TOrder order = new TOrder();
        order.setUserId(userId);
        order.setOrderId(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
        order.setProductId(productId);
        order.setCount(count);
        order.setAmount(amount);
        orderService.save(order);
        return "success";
    }

    @DeleteMapping("delete")
    public String delByOrderId(String orderId){
        orderService.delByOrderId(orderId);
        return "success";
    }
}
