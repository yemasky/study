package com.seata.order.feign;

import com.seata.order.config.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 库存Feign接口
 *
 * @author zxx
 * @throws
 * @return
 * @date 2021/5/28 12:03
 */
@FeignClient(value = "seata-storage-samples")
public interface StorageFeignClient {

    /**
     * @param productId 产品id
     * @return com.seata.order.config.CommonResult
     * @throws
     * @param: count    数量
     * @author zxx
     * @date 2021/5/28 12:03
     */
    @PostMapping("/storage/decrease")
    CommonResult decrease(@RequestParam("productId") Long productId, @RequestParam("count") Integer count);
}
