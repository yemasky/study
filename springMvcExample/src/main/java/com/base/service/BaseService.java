package com.base.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseService {
	protected Map<String, Object> instanceDao = new HashMap<>();
	
	public void freeConnection() throws SQLException {}
}
