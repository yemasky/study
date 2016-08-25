package com.bxg.dual.api.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.jdbc.mysql.DBQuery;

public class UsercenterDao {
	public static List<Map<String, Object>> login(String loginname, String userPassword) throws SQLException {
		HashMap<String, Object> whereCondition = new HashMap<String, Object>();
		whereCondition.put("login_name", loginname);
		List<Map<String, Object>> userInfo = DBQuery.instance("usercenter").table("itcast_user").where(whereCondition)
				.getList();
		return userInfo;
	}
}
