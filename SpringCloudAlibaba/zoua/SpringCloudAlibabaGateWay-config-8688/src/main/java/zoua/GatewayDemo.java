package zoua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayDemo{

	public static void main(String[] args) {
		SpringApplication.run(GatewayDemo.class, args);
	}

}