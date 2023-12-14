package consumer.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import consumer.entity.Order;
import consumer.feignclients.Stock;
import consumer.service.OrderService;
import io.seata.spring.annotation.GlobalTransactional;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Resource
	private Stock stock;

	@Resource
	private OrderService orderService;

	/**
	 * 新增订单
	 * 
	 * @return
	 */
	@RequestMapping("/addOrder")
	@GlobalTransactional // 分布式事务注解，这个一般放在业务层，这里图方便
	public String addOrder() {
		int product_id = 1;
		int order_number = 2;
		Order order = new Order();
		order.setProduct_name("袜子");
		order.setProduct_id(product_id);
		order.setOrder_number(order_number);
		int order_id = orderService.insertOrder(order);
		// System.out.println("订单新增成功-id:" + id);
		// 调用库存扣减
		String result = stock.subStock(order_id, product_id, order_number);
		return result;
	}

}
