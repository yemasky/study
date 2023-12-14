package com.mcy.business.controller;

import com.mcy.business.feign.OrderClient;
import com.mcy.business.feign.ProductClient;
import com.mcy.business.feign.UserClient;
import com.mcy.business.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    /**
     * 下单
     * @param userId
     * @param productId
     * @param count
     * @return
     */
    @GetMapping("booking")
    public String booking(String userId,String productId,Integer count) {
        if(count != null) {
            System.out.println("userId:" + userId + ";productId:" + productId + ";count:" + count);
            return businessService.booking(userId, productId, (int) count);
        }
        return "";

    }

}
