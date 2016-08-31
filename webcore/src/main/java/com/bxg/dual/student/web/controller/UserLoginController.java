package com.bxg.dual.student.web.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.web.api.UsercenterApi;

@Controller
public class UserLoginController extends AbstractController {

	@Override
	public void check(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
	}
	
	@RequestMapping(value = "/login", method=RequestMethod.POST)
	@ResponseBody
	public boolean login(String loginname, String userPassword) {
		boolean isLogin = false;
		try {
			isLogin = UsercenterApi.login(loginname, userPassword);
		} catch (SQLException e) {
			logger.error("error:", e);
			e.printStackTrace();
		}
		return isLogin;
	}
	
	@RequestMapping(value = "/logged", method=RequestMethod.GET)
	@ResponseBody
	public String checkLogin(HttpServletRequest request) {
		return "success";
	}
	
}
