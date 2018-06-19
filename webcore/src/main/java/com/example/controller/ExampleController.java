package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.base.controller.AbstractController;
import com.example.model.ExampleUser;
import com.example.model.ExampleUserToy;

@Controller
public class ExampleController extends AbstractController {
	// 入力检查
	@Override
	public void check(HttpServletRequest request, HttpServletResponse response) {
	}

	//
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
	}

	@RequestMapping(value = "/user/{user_id}")
	public String queryUser(@PathVariable("user_id") int user_id, ModelMap model) {
		ExampleUser user = new ExampleUser();
		user.setUser_id(user_id);
		user.setUser_name("zhaoyang");
		model.addAttribute("user", user);
		return "user_overview";
	}

	@RequestMapping(value = "/ruser/{user_id}")
	@ResponseBody
	public ExampleUser responseUser(@PathVariable("user_id") int user_id, ModelMap model) {
		ExampleUser user = new ExampleUser();
		user.setUser_id(user_id);
		user.setUser_name("zhaoyang");
		return user;
	}
	
	@RequestMapping(value="/jsontype/{user_id}.json",method=RequestMethod.GET)
	public ResponseEntity<ExampleUser> jsonUser(@PathVariable Integer user_id){
		ExampleUser user = new ExampleUser();	
		user.setUser_id(user_id);
		user.setUser_name("zhaoyang");
		List<ExampleUserToy> userListToy = new ArrayList<ExampleUserToy>();
		ExampleUserToy userToy = new ExampleUserToy();
		userToy.setToy_id("1");
		userToy.setToy_name("toy");
		userListToy.add(userToy);
		user.setExampleUserToyList(userListToy);
		return new ResponseEntity<ExampleUser>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/jsontype/{user_id}.xml",method=RequestMethod.GET)
	public ResponseEntity<ExampleUser> xmlUser(@PathVariable Integer user_id){
		ExampleUser user = new ExampleUser();	
		user.setUser_id(user_id);
		user.setUser_name("zhaoyang");
		List<ExampleUserToy> userListToy = new ArrayList<ExampleUserToy>();
		ExampleUserToy userToy = new ExampleUserToy();
		userToy.setToy_id("1");
		userToy.setToy_name("toy");
		userListToy.add(userToy);
		user.setExampleUserToyList(userListToy);
		return new ResponseEntity<ExampleUser>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/json/{user_id}.json",method=RequestMethod.GET)
	@ResponseBody
	public List<String> jsonListUser(@PathVariable Integer user_id){
		
		List<String> exampleList = new ArrayList<String>();
		exampleList.add("1111");
		exampleList.add("2222");
		
		return exampleList;
	}
}



