import java.sql.SQLException;

import example.jdbc.mysql.Config;
import example.jdbc.mysql.MysqlConnection;

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
		// TODO Auto-generated method stub
		Config config = new Config();
		MysqlConnection conn = new MysqlConnection();
		conn.createPools(config);
		conn.getConnection(config.getConnectionName());
		
		//
		
	}

}
