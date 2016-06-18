import java.sql.SQLException;

import jdbc.mysql.Config;
import jdbc.mysql.MysqlConnection;

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
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Config config = new Config();
		MysqlConnection conn = new MysqlConnection();
		conn.createPools(config);
		
		//
		
	}

}
