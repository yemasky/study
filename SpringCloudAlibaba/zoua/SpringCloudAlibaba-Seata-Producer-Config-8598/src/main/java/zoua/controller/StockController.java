package zoua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import zoua.service.StockService;

@RestController
@RequestMapping("/stock")
public class StockController {

	@Value("${server.port}")
	private int port;

	@Autowired
	private StockService stockService;

	@GetMapping("/subStock/{order_id}/{product_id}/{order_number}")
	public String subStock(@PathVariable("order_id") Integer order_id, @PathVariable("product_id") Integer product_id,
			@PathVariable("order_number") Integer order_number) {
		// @RequestBody int order_id, @RequestBody int product_id, @RequestBody int
		// order_number
		System.out.println("进入库存扣减接口:订单ID " + order_id);

		if (order_number > 0) {
			System.out.println("库存扣减成功-id:" + order_number);
			stockService.updateStock(order_number, product_id);
		} else {
			// result = new Result(201,"扣减库存服务-库存失败");
		}
		return "1";
	}

}
