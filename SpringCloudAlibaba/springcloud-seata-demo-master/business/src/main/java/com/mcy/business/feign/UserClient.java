package com.mcy.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "user")
@RequestMapping(value = "user")
public interface UserClient {

    /**
     * 扣减余额
     * @param userId
     * @param money
     * @return
     */
    @PostMapping("debit")
    String debit(@RequestParam("userId") String userId, @RequestParam("money") BigDecimal money) ;
}
