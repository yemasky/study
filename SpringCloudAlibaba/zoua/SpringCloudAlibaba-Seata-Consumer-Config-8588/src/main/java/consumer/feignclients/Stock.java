package consumer.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "Seata-Producer",path = "stock")
public interface Stock {
	//声明需要调用的rest接口对应的方法
    /**
     * 库存扣减
     * @return
     */
	@RequestMapping("/subStock")
    String subStock(int order_id, int product_id, int order_number);
}
