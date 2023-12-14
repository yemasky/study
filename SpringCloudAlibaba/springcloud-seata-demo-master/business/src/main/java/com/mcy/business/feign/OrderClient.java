package com.mcy.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "order")
@RequestMapping(value = "order")
public interface OrderClient {

    @PostMapping("create")
    String create(@RequestParam("userId") String userId, @RequestParam("productId") String productId, @RequestParam("count") int count, @RequestParam("amount") BigDecimal amount);
}
