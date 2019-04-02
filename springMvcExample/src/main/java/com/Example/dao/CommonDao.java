package com.Example.dao;

import java.sql.SQLException;

import core.jdbc.mysql.DBQuery;

public class CommonDao extends DBQuery {
	public CommonDao(String jdbcDsn) throws SQLException {
		super(jdbcDsn);
		// TODO Auto-generated constructor stub
	}

}
