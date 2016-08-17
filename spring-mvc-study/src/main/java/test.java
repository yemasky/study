import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.User;

import jdbc.db.mysql.DBQuery;

/**
 * 
 */

/**
 * @author admin
 *
 */
public class test {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//ConnectionPoolManager ss = new ConnectionPoolManager();
		//ss.init();
		// TODO Auto-generated method stub
		/*
		 * Config config = new Config(); MysqlConnection conn = new
		 * MysqlConnection(); conn.createPools(config);
		 * conn.getConnection(config.getConnectionName());
		 */

		//
		String jdbcDsn = "test";
		HashMap<String, String> whereData = new HashMap<String, String>();
		whereData.put("uid", "2");
		//whereData.put("username", "11");
		List<Map<String, Object>> DDIV = DBQuery.instance(jdbcDsn).table("users").where(whereData).getList();
		System.out.println(DDIV.size());
		if(DDIV.size() > 0) System.out.println(DDIV.get(0).get("uid"));
		
		HashMap<String, String> insertData = new HashMap<String, String>();
		insertData.put("username", "1122");
		insertData.put("password", "111111");
		DBQuery.instance(jdbcDsn).table("users").setInsertData(insertData).insert();
		
		HashMap<String, String> updateData = new HashMap<String, String>();
		updateData.put("username", "11223333");
		updateData.put("password", "111111");
		DBQuery.instance(jdbcDsn).table("users").where(whereData).setUpdateData(updateData).update();
		
		DBQuery.instance(jdbcDsn).table("users").where(whereData).setUpdateData(updateData).update();
		
		DBQuery.instance(jdbcDsn).table("users").where(whereData).setUpdateData(updateData).update();
		
		DDIV = DBQuery.instance(jdbcDsn).table("users").where(whereData).getList();
		
		List<Users> ss = DBQuery.instance(jdbcDsn).table(Users.class).where(whereData).getEntityList();
		System.out.println(ss.get(0).getUsername());
		
		Users aaa = new Users();
		aaa.setPassword("111");
		aaa.setUsername("233");
		DBQuery.instance(jdbcDsn).insert(aaa);
		
		DBQuery.instance(jdbcDsn).insert(aaa);

	}

}
