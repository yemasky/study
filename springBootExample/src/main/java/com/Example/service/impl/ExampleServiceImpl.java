package com.Example.service.impl;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.Example.config.Config;
import com.Example.dao.ExamplelDao;
import com.Example.model.Entity.Test;
import com.Example.service.ExampleService;
import com.base.service.BaseService;

public class ExampleServiceImpl extends BaseService implements ExampleService {
	private ExamplelDao examplelDao;

	public ExampleServiceImpl() throws SQLException {
		examplelDao = new ExamplelDao(Config.test);
	}

	@Override
	public Test geTest(int id) throws SQLException {
		// TODO Auto-generated method stub
		return examplelDao.geTest(id);
	}

	@Override
	public List<Test> geTest(int[] id) throws SQLException {
		// TODO Auto-generated method stub
		return examplelDao.geTest(id);
	}

	@Override
	public BigInteger saveTest(Test test) throws Exception {
		// TODO Auto-generated method stub
		return examplelDao.saveTest(test);
	}

	@Override
	public BigInteger saveTest(HashMap<String, Object> insertData) throws Exception {
		// TODO Auto-generated method stub
		return examplelDao.saveTest(insertData);
	}

	@Override
	public void freeConnection() throws SQLException {
		// TODO Auto-generated method stub
		examplelDao.freeConnection();
	}

	@Override
	public void setTransaction(boolean isTransaction) throws SQLException {
		// TODO Auto-generated method stub
		examplelDao.setTransaction(isTransaction);
	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		examplelDao.commit();
	}

	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		examplelDao.rollback();
	}
	
	@Override
	public void rollbackAndFreeConnection() throws SQLException {
		// TODO Auto-generated method stub
		examplelDao.rollback();
		examplelDao.setTransaction(false);
		examplelDao.freeConnection();		
	}

	@Override
	protected void finalize() throws SQLException {
		// TODO Auto-generated method stub
		examplelDao.freeConnection();
	}

}
