package com.MpEnterprise.dao;

import java.sql.SQLException;

import com.Example.config.Config;

import core.jdbc.mysql.DBQuery;

public class CommonDao {
	public static CommonDao instance() {
		return new CommonDao();
	}
	
	public void closeTestConnection() throws SQLException {
		DBQuery.instance(Config.test).closeConnection();
	}
	
	public void closeAllConnection() throws SQLException {
		DBQuery.instance(Config.test).closeAllConnection();
	}
}
