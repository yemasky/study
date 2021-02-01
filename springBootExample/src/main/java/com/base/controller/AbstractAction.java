package com.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.base.type.ErrorCode;
import com.base.type.Success;

public abstract class AbstractAction {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Success success = new Success();
	protected final String CLIENT_KEY = "client_key";
	protected final String SESSION_KEY = "session_key";
	protected String client_key;
	protected HttpSession httpSession;

	public abstract void check(HttpServletRequest request, HttpServletResponse response);

	public abstract void service(HttpServletRequest request, HttpServletResponse response) throws Exception;
	// 资源回收
	public abstract void release(HttpServletRequest request, HttpServletResponse response) throws Exception;
	// 事務回滾
	public abstract void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public Success doDefault(HttpServletRequest request, HttpServletResponse response) {
		return success;
	}
	
	public Success excute(HttpServletRequest request, HttpServletResponse response) {
		try {
			this.client_key = (String) request.getAttribute(CLIENT_KEY);
			this.httpSession = (HttpSession) request.getAttribute(SESSION_KEY);
			this.check(request, response);
			this.service(request, response);
			this.release(request, response);
		} catch (Exception e) {
			success.setSuccess(false);
			success.setCode(ErrorCode.__F_SYS);
			try {
				this.rollback(request, response);
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				MDC.put("APP_NAME", "web_error");
				logger.error(success.getMessage(), ex);
			}
			// TODO Auto-generated catch block
			MDC.put("APP_NAME", "web_error");
			logger.error(success.getMessage(), e);
		}
		
		return success;
	}
	
}
