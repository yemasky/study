import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import example.jdbc.mysql.Config;
import example.jdbc.mysql.MysqlConnection;
import jdbc.db.mysql.ConnectionPoolManager;

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
		ConnectionPoolManager ss = new ConnectionPoolManager();
		ss.loadDrivers();
		// TODO Auto-generated method stub
		/*
		 * Config config = new Config(); MysqlConnection conn = new
		 * MysqlConnection(); conn.createPools(config);
		 * conn.getConnection(config.getConnectionName());
		 */

		//

	}

}
