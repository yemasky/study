import java.sql.SQLException;

import org.junit.Test;

import jdbc.mysql.Config;
import jdbc.mysql.MysqlConnection;

public class TestMysqlConnection {
	
	@Test
	public void testmysql() throws SQLException{
		Config config = new Config();
		MysqlConnection conn = new MysqlConnection();
		conn.createPools(config);
	}

}
