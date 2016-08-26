import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;

import com.bxg.dual.student.web.model.ExampleUser;
import com.bxg.dual.student.web.model.ExampleUser2;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import core.jdbc.mysql.DBQuery;



/**
 * 
 */

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
		HashMap<String, Object> whereData = new HashMap<String, Object>();
		whereData.put("uid", "2");
		//whereData.put("username", "222");
		//whereData.put("username", "11");
		List<Map<String, Object>> DDIV = DBQuery.instance(jdbcDsn).table("ExampleUser").where(whereData).getList();
		System.out.println(DDIV.size());
		if(DDIV.size() > 0) System.out.println(DDIV.get(0).get("uid"));
		
		ExampleUser user = (ExampleUser) DBQuery.instance(jdbcDsn).table(ExampleUser.class).where(whereData).getEntity();
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
		
		ExampleUser2 aaa2 = new ExampleUser2();
		aaa2.setUid(core.util.Encrypt.getRandomUUID());
		aaa2.setPassword("111");
		aaa2.setUsername("2323");
		DBQuery.instance(jdbcDsn).insert(aaa2);
		
		//LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
	    //StatusPrinter.print(lc);
		
		//DBQuery.instance(jdbcDsn).table("users").delete();
		
		//DBQuery.instance(jdbcDsn).insert(aaa);

	}

}
