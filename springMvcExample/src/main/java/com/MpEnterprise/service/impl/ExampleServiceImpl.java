package com.MpEnterprise.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.Example.dao.ExamplelDao;
import com.Example.model.Entity.Test;
import com.Example.service.ExampleService;

public class ExampleServiceImpl implements ExampleService {

	@Override
	public Test geTest(int id) throws SQLException {
		// TODO Auto-generated method stub
		return ExamplelDao.instance().geTest(id);
	}

	@Override
	public List<Test> geTest(int[] id) throws SQLException {
		// TODO Auto-generated method stub
		return ExamplelDao.instance().geTest(id);
	}

	@Override
	public long saveTest(Test test) throws SQLException, Exception {
		// TODO Auto-generated method stub
		return ExamplelDao.instance().saveTest(test);
	}

	@Override
	public long saveTest(HashMap<String, Object> insertData) throws SQLException {
		// TODO Auto-generated method stub
		return ExamplelDao.instance().saveTest(insertData);
	}

	
	
}
