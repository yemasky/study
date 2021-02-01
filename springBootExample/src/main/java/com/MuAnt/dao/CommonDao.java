package com.MuAnt.dao;

public class CommonDao {
	private static CommonDao instanceDao = null;

	public static CommonDao instance() {
		if (instanceDao == null)
			instanceDao = new CommonDao();
		return instanceDao;
	}
	
	
}
