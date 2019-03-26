package com.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.base.type.ErrorCode;
import com.base.type.Success;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public abstract class AbstractAction {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected static XmlMapper xml = new XmlMapper();
	protected static ObjectMapper mapper = new ObjectMapper();
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected Success successType = new Success();


	public abstract void check(HttpServletRequest request, HttpServletResponse response);

	public abstract void service(HttpServletRequest request, HttpServletResponse response);
	// 资源回收 事務回滾
	public abstract void release(HttpServletRequest request, HttpServletResponse response);
	
	public Success doDefault(HttpServletRequest request, HttpServletResponse response) {
		return successType;
	}
	
	public Success excute(HttpServletRequest request, HttpServletResponse response) {
		try {
			this.check(request, response);
			this.service(request, response);
		} catch (Exception e) {
			successType.setSuccess(false);
			successType.setErrorCode(ErrorCode.__F_SYS);
			this.release(request, response);
			// TODO Auto-generated catch block
			MDC.put("APP_NAME", "web_error");
			logger.error(successType.getMessage(), e);
		}
		
		return successType;
	}
	
}
