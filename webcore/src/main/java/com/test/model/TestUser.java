package com.test.model;

import java.util.List;

public class TestUser {
	private int user_id;
	private String user_name;
	private String password;
	private List<TestUserToy> TestUserToyList;
	
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

	public List<TestUserToy> getExampleUserToyList() {
		return TestUserToyList;
	}

	public void setExampleUserToyList(List<TestUserToy> testUserToyList) {
		TestUserToyList = testUserToyList;
	}
	

}
