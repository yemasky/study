package com.Example.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.Example.model.Entity.Test;

public interface ExampleService {
	public Test geTest(int id) throws SQLException;
	
	public List<Test> geTest(int[] id) throws SQLException;
	
	public long saveTest(Test test) throws Exception; 

	public long saveTest(HashMap<String, Object> insertData) throws Exception; 
}
