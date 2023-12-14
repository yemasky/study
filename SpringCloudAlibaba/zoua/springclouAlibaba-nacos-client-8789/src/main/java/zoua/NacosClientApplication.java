package zoua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // 开启服务的注册(可以省略不写)
public class NacosClientApplication {
	public static void main(String[] args) {
		SpringApplication.run(NacosClientApplication.class, args);
	}
}
