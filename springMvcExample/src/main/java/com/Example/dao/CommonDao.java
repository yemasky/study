package com.Example.dao;

import java.sql.SQLException;

import com.Example.config.Config;

import core.jdbc.mysql.DBQuery;

public class CommonDao {
	private static CommonDao instanceDao = null;

	public static CommonDao instance() {
		if (instanceDao == null)
			instanceDao = new CommonDao();
		return instanceDao;
	}

	public void closeTestConnection() throws SQLException {
		DBQuery.instance(Config.test).closeConnection();
	}

	public void closeAllConnection() throws SQLException {
		DBQuery.instance(Config.test).closeAllConnection();
	}

	public void freeConnection() throws SQLException {
		DBQuery.instance(Config.test).freeConnection();
	}
	
	public void commit() throws SQLException {
		DBQuery.instance(Config.test).commit();
	}
	
	public void rollback() throws SQLException {
		DBQuery.instance(Config.test).rollback();
	}
}
