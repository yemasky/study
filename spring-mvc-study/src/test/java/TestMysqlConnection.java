import java.sql.SQLException;

import org.junit.Test;

import example.jdbc.mysql.Config;
import example.jdbc.mysql.MysqlConnection;

public class TestMysqlConnection {
	
	@Test
	public void testmysql() throws SQLException{
		Config config = new Config();
		MysqlConnection conn = new MysqlConnection();
		conn.createPools(config);
	}

}
