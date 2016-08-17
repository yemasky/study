package com.imooc.mvcdemo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseController {
	protected abstract void check(HttpServletRequest request, HttpServletResponse response);
	protected abstract void service(HttpServletRequest request, HttpServletResponse response);
	public void excute(HttpServletRequest request, HttpServletResponse response) {
		this.check(request, response);
		this.service(request, response);
	}
}
