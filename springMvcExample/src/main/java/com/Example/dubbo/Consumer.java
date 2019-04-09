package com.Example.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.Example.dubbo.service.DemoService;

public class Consumer {
    public static void main(String[] args) throws Exception {
        @SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"dubbo/consumer.xml"});
        context.start();
        DemoService demoService = (DemoService)context.getBean("demoService"); // 获取远程服务代理
        String hello = demoService.sayHello("world"); // 执行远程方法
        System.out.println( hello ); // 显示调用结果
    }
}
