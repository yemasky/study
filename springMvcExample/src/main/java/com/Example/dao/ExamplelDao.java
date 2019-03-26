package com.Example.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Example.config.Config;
import com.Example.model.Entity.Test;

import core.jdbc.mysql.DBQuery;
import core.jdbc.mysql.WhereRelation;
import core.jdbc.mysql.whereCriteria;

public class ExamplelDao {
	
	public static ExamplelDao instance() {
		return new ExamplelDao();
	}
	
	public Test geTest(int id) throws SQLException {
		HashMap<String, Object> whereSQL = new HashMap<String, Object>();
		whereSQL.put("id", id);
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.where(whereCriteria.EQ, whereSQL);
		Test test = DBQuery.instance(Config.test).table(Test.class).getEntity(whereRelation);
		return test;
	}
	
	public List<Test> geTest(int[] id) throws SQLException {
		HashMap<String, Object> whereSQL = new HashMap<String, Object>();
		whereSQL.put("id", id);
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.where(whereCriteria.IN, whereSQL);
		List<Test> test = DBQuery.instance(Config.test).table(Test.class).getEntityList(whereRelation);
		return test;
	}
	
	public long saveTest(Test test) throws SQLException, Exception {
		return (long) DBQuery.instance(Config.test).insertIdEntity(test);
	}
	
	public long saveTest(HashMap<String, Object> insertData) throws SQLException {
		return (long) DBQuery.instance(Config.test).table("test").insertIdEntity(insertData);
	}
	
	//
	public void example() throws SQLException {
		HashMap<String, Object> whereSQL = new HashMap<String, Object>();
		whereSQL.put("book_order_number_ourter", "");
			
		//Book book = DBQuery.instance(dsn).table(Book.class).getEntity(WhereRelation.instance().where(whereSQL));
		
		Map<whereCriteria, HashMap<String, Object>> whereSQL2 = new HashMap<whereCriteria, HashMap<String, Object>>();
		HashMap<String, Object> whereEQ = new HashMap<String, Object>();
		whereEQ.put("book_id", "");
		//whereEQ.put("book_change", "0");
		whereSQL2.put(whereCriteria.EQ, whereEQ);
		HashMap<String, Object> whereNE = new HashMap<String, Object>();
		whereNE.put("book_order_number_ourter", "");
		whereSQL2.put(whereCriteria.NE, whereNE);
		
		
	}
	
}


