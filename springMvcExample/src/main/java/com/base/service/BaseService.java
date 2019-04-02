package com.base.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseService {
	protected Map<String, Object> instanceDao = new HashMap<>();
	
	public abstract void setTransaction(boolean isTransaction) throws SQLException;
	
	public abstract void commit() throws SQLException;
	
	public abstract void rollback() throws SQLException;
	
	public abstract void freeConnection() throws SQLException;
	
	protected abstract void finalize() throws SQLException;
}
