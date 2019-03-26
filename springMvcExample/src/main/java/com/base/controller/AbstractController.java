package com.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected static XmlMapper xml = new XmlMapper();
	protected static ObjectMapper mapper = new ObjectMapper();
	protected HttpServletRequest request;
	protected HttpServletResponse response;

	public abstract void beforeCheck(HttpServletRequest request, HttpServletResponse response);

	public abstract void afterCheck(HttpServletRequest request, HttpServletResponse response);

	public abstract String defaultAction(HttpServletRequest request, HttpServletResponse response);

	public AbstractController() {
	
	}

	@ModelAttribute
	public void excuseController(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		this.request = httpRequest;
		this.response = httpResponse;
		this.beforeCheck(this.request, this.response);
	}

}
