package com.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.ModelAttribute;

public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public abstract void check(HttpServletRequest request, HttpServletResponse response);

	public abstract void service(HttpServletRequest request, HttpServletResponse response);
	
	public void checkLogin(HttpServletRequest request, HttpServletResponse response){
		
	}

	@ModelAttribute
	public void excuse(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		this.request = httpRequest;
		this.response = httpResponse;
		this.check(this.request, this.response);
		this.service(this.request, this.response);
	}
}
