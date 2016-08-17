package com.imooc.mvcdemo.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class IndexController {
	public void index(HttpServletRequest request, HttpServletResponse response) {
		String model = request.getParameter("model");
		Class<?>[] SSICommand = model.getClass().getInterfaces();
	}
}
