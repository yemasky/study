package zoua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zoua.service.StockService;


@RestController
@RequestMapping("/stock")
public class StockController {
	@Value("${server.port}")
    private String port;

	@Autowired
    private StockService stockService;
    /**
     * 库存扣减
     * @return
     */
    @PostMapping("/subStock")
    public String subStock() {
        //@RequestBody int order_id, @RequestBody int product_id, @RequestBody int order_number
        int order_id = 1;
        int product_id = 1;
        int order_number = 1;
        System.out.println("进入库存扣减接口");
        
        if(order_number > 0){
            System.out.println("库存扣减成功-id:" + order_number);
            stockService.updateStock(order_number, product_id);
        }else {
            //result = new Result(201,"扣减库存服务-库存失败");
        }
        return "1";
    }
}
