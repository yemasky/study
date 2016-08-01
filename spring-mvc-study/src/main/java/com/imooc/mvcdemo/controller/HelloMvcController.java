package com.imooc.mvcdemo.controller;

//import java.sql.SQLException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jdbc.db.mysql.ConnectionPoolManager;

//告诉DispatcherServlet相关的容器， 这是一个Controller， 管理好这个bean哦
@Controller
//类级别的RequestMapping，告诉DispatcherServlet由这个类负责处理以跟URL。
//HandlerMapping依靠这个标签来工作
@RequestMapping("/hello")
public class HelloMvcController {
	
	//方法级别的RequestMapping， 限制并缩小了URL路径匹配，同类级别的标签协同工作，最终确定拦截到的URL由那个方法处理
	@RequestMapping("/mvc")
	public String helloMvc() throws Exception {
		ConnectionPoolManager connectionPoolManager = new ConnectionPoolManager();
		connectionPoolManager.init();
		connectionPoolManager.getConnection("test.write");
		//connectionPoolManager.getConnection("test");
		connectionPoolManager.getConnection("test.read");
		connectionPoolManager.getConnection("test.read.002");
		//connectionPoolManager.getConnection("mytest");
		connectionPoolManager.releaseConnection("mytest.read.001");
		//视图渲染，/WEB-INF/jsps/home.jsp
		return "home";
	}

}
