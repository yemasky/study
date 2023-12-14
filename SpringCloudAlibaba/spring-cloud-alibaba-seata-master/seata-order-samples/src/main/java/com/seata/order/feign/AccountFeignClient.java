package com.seata.order.feign;

import com.seata.order.config.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * 用户模块Feign接口
 *
 * @author zxx
 * @date 2021/5/28 11:44
 */
@FeignClient(value = "seata-account-samples")
public interface AccountFeignClient {

    /**
     * 扣减账户余额
     *
     * @param userId 用户ID
     * @return com.seata.order.config.CommonResult
     * @param: money 钱
     * @author zxx
     * @date 2021/5/28 11:44
     */
    @PostMapping("/account/decrease")
    CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
