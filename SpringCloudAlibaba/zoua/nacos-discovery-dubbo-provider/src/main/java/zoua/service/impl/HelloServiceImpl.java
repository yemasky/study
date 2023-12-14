package zoua.service.impl;

import org.apache.dubbo.config.annotation.DubboService;

import zoua.service.IHelloService;

@DubboService
public class HelloServiceImpl implements IHelloService {

	@Override
	public String hello(String name) {
		// TODO Auto-generated method stub
		 return "Hello:"+name;
	}

}
