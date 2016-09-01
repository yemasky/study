import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.web.model.ExampleUser;
import com.web.model.ExampleUser2;

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
		HashMap<String, Object> whereData = new HashMap<String, Object>();
		whereData.put("uid", "2");
		
		List<Map<String, Object>> DDIV = DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereData).getList();
		System.out.println(DDIV.size());
		if(DDIV.size() > 0) System.out.println(DDIV.get(0).get("uid"));
		
		ExampleUser user = DBQuery.instance(jdbcDsn).table(ExampleUser.class).where(whereData).getEntity();
		if(user != null)System.out.println(user.getUid());
		
		HashMap<String, String> insertData = new HashMap<String, String>();
		insertData.put("username", "1122");
		insertData.put("password", "111111");
		DBQuery.instance(jdbcDsn).table("ExampleUser").setInsertData(insertData).insert();
		
		HashMap<String, String> updateData = new HashMap<String, String>();
		updateData.put("username", "11223333");
		updateData.put("password", "111111");
		DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereData).setUpdateData(updateData).update();
		
		DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereData).setUpdateData(updateData).update();
		updateData.put("username", "11223333'");
		DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereData).setUpdateData(updateData).update();
		
		DDIV = DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereData).getList();
		
		List<ExampleUser> ss = DBQuery.instance(jdbcDsn).table(ExampleUser.class).where(whereData).getEntityList();
		if(ss.size() > 0) System.out.println(ss.get(0).getUsername());
		
		ExampleUser aaa = new ExampleUser();
		aaa.setPassword("111");
		aaa.setUsername("233");
		DBQuery.instance(jdbcDsn).insert(aaa);
		
		ExampleUser2 user2 = new ExampleUser2();
		user2.setUid(core.util.Encrypt.getRandomUUID());
		user2.setPassword("111");
		user2.setUsername("2323");
		DBQuery.instance(jdbcDsn).insert(user2);
		
		//LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	    //StatusPrinter.print(lc);
	}

}
