package springCloudDemo.CloudConfigClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class ConfigClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigClientApplication.class, args);
	}
}

//如果refresh命令可以发送给config server，然后config server自动通知所有config client， 那么就可以大大简化配置刷新工作。
//这样，虽然仍然需要通过refresh命令触发， 但通过webhook等钩子方式， 我们只需要将关联的命令挂到配置中心上，而不需要每个配置客户端都去关联。
//现在，我们通过整合消息队列Rabbitmq来完成这件事
//Spring Cloud Netflix Bus是Spring Cloud的消息机制,当Git Repository 改变时,通过POST请求Config Server的/bus/refresh,
//Config Server 会从repository获取最新的信息并通过amqp传递给client,如图所示.