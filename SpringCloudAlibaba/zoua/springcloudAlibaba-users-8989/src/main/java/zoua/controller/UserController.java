package zoua.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import zoua.feignclients.ProductClient;

@RestController
public class UserController {

	@Autowired
	private ProductClient productClient;

	@GetMapping("/invoke")
	public String invokeProduct() {
		String result = productClient.product(21);
		return "调用用户服务成功.....调用商品服务结果：{}" + result;
	}
}
