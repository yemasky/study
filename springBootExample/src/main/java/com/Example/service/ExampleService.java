package com.Example.service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.Example.model.Entity.Test;

public interface ExampleService {
	public Test geTest(int id) throws SQLException;
	
	public List<Test> geTest(int[] id) throws SQLException;
	
	public BigInteger saveTest(Test test) throws Exception; 

	public BigInteger saveTest(HashMap<String, Object> insertData) throws Exception; 
}
