package com.MpEnterprise.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.base.controller.AbstractAction;
import com.base.type.ErrorCode;
import com.base.type.Success;

public class LoginAction extends AbstractAction {
	@Override
	public void check(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
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
	
	public Success doDefault(HttpServletRequest request, HttpServletResponse response) {
		return successType;
	}
	//http://localhost:8081/wkllme-mp-Enterprise/mp/app.json?module=login&action=login&method=login
	public Success doLogin(HttpServletRequest request, HttpServletResponse response) {
		this.successType.setErrorCode(ErrorCode.__T_LOGIN);
		return this.successType;
	}

}
