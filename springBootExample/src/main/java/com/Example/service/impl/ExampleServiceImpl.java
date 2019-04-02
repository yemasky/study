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

public class ExampleServiceImpl extends BaseService implements ExampleService  {
	
	public ExampleServiceImpl() throws SQLException {
		this.instanceDao.put(Config.test, new ExamplelDao(Config.test));
	}
	
	@Override
	public Test geTest(BigInteger id) throws SQLException {
		// TODO Auto-generated method stub
		return ((ExamplelDao) this.instanceDao.get(Config.test)).geTest(id);
	}

	@Override
	public List<Test> geTest(int[] id) throws SQLException {
		// TODO Auto-generated method stub
		return ((ExamplelDao) this.instanceDao.get(Config.test)).geTest(id);
	}

	@Override
	public BigInteger saveTest(Test test) throws Exception {
		// TODO Auto-generated method stub
		return ((ExamplelDao) this.instanceDao.get(Config.test)).saveTest(test);
	}

	@Override
	public BigInteger saveTest(HashMap<String, Object> insertData) throws Exception {
		// TODO Auto-generated method stub
		return ((ExamplelDao) this.instanceDao.get(Config.test)).saveTest(insertData);
	}

	@Override
	public void freeConnection() throws SQLException {
		// TODO Auto-generated method stub
		((ExamplelDao) this.instanceDao.get(Config.test)).freeConnection();
	}

	@Override
	public void setTransaction(boolean isTransaction) throws SQLException {
		// TODO Auto-generated method stub
		((ExamplelDao) this.instanceDao.get(Config.test)).setTransaction(isTransaction);
	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		((ExamplelDao) this.instanceDao.get(Config.test)).commit();
	}

	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		((ExamplelDao) this.instanceDao.get(Config.test)).rollback();
	}

	@Override
	protected void finalize() throws SQLException   {
		// TODO Auto-generated method stub
		((ExamplelDao) this.instanceDao.get(Config.test)).freeConnection();
	}
}
