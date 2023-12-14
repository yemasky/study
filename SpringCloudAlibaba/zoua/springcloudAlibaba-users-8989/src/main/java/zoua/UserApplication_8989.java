package zoua;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients // 开启OpenFeign调用支持
public class UserApplication_8989 {
	public static void main(String[] args) {
		SpringApplication.run(UserApplication_8989.class, args);
	}
}
