package com.mcy.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product")
@RequestMapping(value = "product")
public interface ProductClient {

    @PostMapping("deduct")
    String deduct(@RequestParam("productId") String productId, @RequestParam("count") int count) ;
}
