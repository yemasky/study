package com.bxg.dual.student.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bxg.dual.student.model.ExampleUser;


@Controller
public class ExampleController extends AbstractController {
	//入力检查
	@Override
	public void check(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("check");
		System.out.println(request.toString());
	}
	
	//
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("service");
		System.out.println(request.toString());
		
	}
	
	@RequestMapping(value = "/user/{userid}")
	@ResponseBody
	public ExampleUser queryUser(@PathVariable("userid") int userID, ModelMap model) {
		ExampleUser u = new ExampleUser();
		u.setUid(userID);
		u.setUsername("zhaoyang");
		model.addAttribute("User", u);
		return u;
	}
}