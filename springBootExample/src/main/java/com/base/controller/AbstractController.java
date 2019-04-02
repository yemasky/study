package com.base.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import core.util.Cookies;
import core.util.Encrypt;

public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession httpSession;
	protected final String CLIENT_KEY = "client_key";
	protected final String SESSION_KEY = "session_key";

	public abstract void beforeCheck(HttpServletRequest request, HttpServletResponse response);

	public abstract void release(HttpServletRequest request, HttpServletResponse response);

	public abstract String defaultAction(HttpServletRequest request, HttpServletResponse response);
	
	public AbstractController() {
	
	}

	@ModelAttribute
	public void excuseController(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		this.request = httpRequest;
		this.response = httpResponse;
		//为每个用户产生唯一session
		httpSession = this.request.getSession(true);
		httpRequest.setAttribute(CLIENT_KEY, Encrypt.md5Lower(Encrypt.getRandomUUID() + System.currentTimeMillis() + Math.random()));
		httpRequest.setAttribute(SESSION_KEY, httpSession);
		this.beforeCheck(this.request, this.response);
	}
	
	public Cookie getCookie(String name) {
		return Cookies.getCookie(request, name);
	}
	
	public void setCookie(String name, String value) {
		Cookies.setCookie(response, name, value);
	}
	
	public String getSessionId() {
		return httpSession.getId();
	}
}
