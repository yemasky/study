package com.MpEnterprise.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.controller.AbstractAction;
import com.base.type.ErrorCode;

public class IndexAction extends AbstractAction {

	@Override
	public void check(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		System.out.println("action check");
		
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String method = request.getParameter("method");
		if(method == null) method = "";
		//
		switch (method) {
		case "login":
			this.doLogin(request, response);
			break;

		default:
			this.doDefault(request, response);
			break;
		}
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void doLogin(HttpServletRequest request, HttpServletResponse response) {
		this.successType.setErrorCode(ErrorCode.__T_LOGIN);
	}


	
}
