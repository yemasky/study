package com.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.api.dao.UsercenterDao;

import core.util.Encrypt;

public class UsercenterApi extends AbstractApi {
	public static boolean login(String loginname, String userPassword) throws SQLException {
		List<Map<String, Object>> userInfo = UsercenterDao.login(loginname, userPassword);
		String salt = (String) userInfo.get(0).get("salt");
		String password = (String) userInfo.get(0).get("password");
		if(password.equals(Encrypt.entryptText(userPassword, salt))) {
			return true;
		}
		return false;
	}
}
