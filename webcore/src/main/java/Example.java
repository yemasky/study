import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.model.ExampleUser;
import com.example.model.ExampleUserToy;

import core.jdbc.mysql.DBQuery;

/**
 * @author admin
 *
 */
public class Example {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//
		String jdbcDsn = "test";
		HashMap<String, Object> whereSQL = new HashMap<String, Object>();
		whereSQL.put("user_id", "2");
		
		List<Map<String, Object>> DDIV = DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereSQL).getList();
		System.out.println(DDIV.size());
		if(DDIV.size() > 0) System.out.println(DDIV.get(0).get("user_id"));
		
		ExampleUser user = DBQuery.instance(jdbcDsn).table(ExampleUser.class).where(whereSQL).getEntity();
		if(user != null)System.out.println(user.getUser_id());
		
		HashMap<String, String> insertData = new HashMap<String, String>();
		insertData.put("user_name", "1122");
		insertData.put("password", "111111");
		DBQuery.instance(jdbcDsn).table("ExampleUser").setInsertData(insertData).insert();
		
		HashMap<String, String> updateData = new HashMap<String, String>();
		updateData.put("user_name", "11223333");
		updateData.put("password", "111111");
		DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereSQL).setUpdateData(updateData).update();
		
		DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereSQL).setUpdateData(updateData).update();
		updateData.put("user_name", "11223333'");
		DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereSQL).setUpdateData(updateData).update();
		
		DDIV = DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereSQL).getList();
		
		List<ExampleUser> ss = DBQuery.instance(jdbcDsn).table(ExampleUser.class).where(whereSQL).getEntityList();
		if(ss.size() > 0) System.out.println(ss.get(0).getUser_name());
		
		ExampleUser aaa = new ExampleUser();
		aaa.setPassword("111");
		aaa.setUser_name("233");
		DBQuery.instance(jdbcDsn).insert(aaa);
		
		ExampleUserToy userToy = new ExampleUserToy();
		userToy.setToy_id(core.util.Encrypt.getRandomUUID());
		userToy.setToy_name("111");
		DBQuery.instance(jdbcDsn).insert(userToy);
		
		//LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	    //StatusPrinter.print(lc);
	}

}
