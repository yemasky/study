package ServiceRibbon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class HelloService {
	@Autowired RestTemplate restTemplate;
	
	//@HystrixCommand注解标注访问服务的方法
	@HystrixCommand(fallbackMethod = "serviceFailure")
	public String getHelloContent() {
		return restTemplate.getForObject("http://SERVICE-HELLOWORLD/", String.class);
	}
	
	public String serviceFailure() {
		return "hello world service is not available !";
	}
}
