package com.example.model;

import java.util.List;

public class ExampleUser {
	private int user_id;
	private String user_name;
	private String password;
	private List<ExampleUserToy> ExampleUserToyList;
	
	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<ExampleUserToy> getExampleUserToyList() {
		return ExampleUserToyList;
	}

	public void setExampleUserToyList(List<ExampleUserToy> exampleUserToyList) {
		ExampleUserToyList = exampleUserToyList;
	}
	

}
