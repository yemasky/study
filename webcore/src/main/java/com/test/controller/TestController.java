package com.test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.base.controller.AbstractController;
import com.example.model.ExampleUser;

@Controller
public class TestController extends AbstractController {
	// 入力检查
	@Override
	public void check(HttpServletRequest request, HttpServletResponse response) {
	}

	//
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
	}

	@RequestMapping(value = "/test/{user_id}")
	public String queryUser(@PathVariable("user_id") int user_id, ModelMap model) {
		ExampleUser user = new ExampleUser();
		user.setUser_id(user_id);
		user.setUser_name("zhaoyang");
		model.addAttribute("user", user);
		return "user_overview";
	}

	
}



