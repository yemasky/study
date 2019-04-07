package com.base.service;

import java.sql.SQLException;

public abstract class BaseService {
	
	public abstract void setTransaction(boolean isTransaction) throws SQLException;
	
	public abstract void commit() throws SQLException;
	
	public abstract void rollback() throws SQLException;
	
	public abstract void freeConnection() throws SQLException;
	
	public abstract void rollbackAndFreeConnection() throws SQLException;
	
	protected abstract void finalize() throws SQLException;
}
