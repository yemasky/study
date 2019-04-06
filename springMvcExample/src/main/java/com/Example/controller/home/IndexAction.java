package com.Example.controller.home;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Example.model.Entity.Test;
import com.Example.service.impl.ExampleServiceImpl;
import com.base.controller.AbstractAction;
import com.base.type.ErrorCode;

public class IndexAction extends AbstractAction {
	private ExampleServiceImpl exampleService;
	
	@Override
	public void check(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("action check"); 
		   
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String method = (String) request.getAttribute("method");
		if(method == null) method = "";
		//
		switch (method) {
		case "login":
			this.doLogin(request, response);
			break;
		case "test":
			this.doTest(request, response);
			break;
		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		if(exampleService != null) exampleService.freeConnection();
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		exampleService.rollback();
		exampleService.setTransaction(false);
	}
	
	public void doLogin(HttpServletRequest request, HttpServletResponse response) {
		
		this.successType.setErrorCode(ErrorCode.__T_LOGIN);
	}

	public void doTest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		exampleService = new ExampleServiceImpl();
		SimpleDateFormat toDay = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String ssString = toDay.format(System.currentTimeMillis());
		System.out.println(ssString);
		HashMap<String, Object> insertData = new HashMap<>();
		insertData.put("title", "你好");
		insertData.put("description", "描述");
		insertData.put("add_datetime", ssString);
		exampleService.saveTest(insertData);
		
		exampleService.setTransaction(true);
		Timestamp add_datetime = new Timestamp(System.currentTimeMillis());
		Test test = new Test();
		test.setTitle("你好");
		test.setDescription("描述2");
		test.setAdd_datetime(add_datetime);
		exampleService.saveTest(test);
		exampleService.rollback();
		exampleService.setTransaction(false);
		
		//exampleService.freeConnection();
		
		this.successType.setErrorCode(ErrorCode.__T_SUCCESS);
	}

	
}
