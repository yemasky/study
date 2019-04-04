package springCloudDemo.CloudServiceFeign;

import org.springframework.stereotype.Component;

@Component
public class HelloWordServiceFailure implements HelloWorldService {

	@Override
	public String sayHello() {
		// TODO Auto-generated method stub
		System.out.println("hello word service is not available!");
		return "hello word service is not available!";
	}

}
