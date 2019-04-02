package com.MpEnterprise.dao;

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
	
	
}
