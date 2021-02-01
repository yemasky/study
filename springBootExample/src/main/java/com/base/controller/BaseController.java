package com.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController extends AbstractController {

	@Override
	public void beforeCheck(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	@RequestMapping(value = "/")
	public String defaultAction(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return "请求错误！";
	}

}
