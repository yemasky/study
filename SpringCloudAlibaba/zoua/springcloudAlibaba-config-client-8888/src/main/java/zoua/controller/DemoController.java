package zoua.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope//允许远端配置修改后自动刷新
public class DemoController {

	@Value("${constomer.username}")
	private String username;

	@GetMapping("/demo")
	public String demo() {
		return "demo ok！！！" + username;
	}
}