import java.io.IOException;
import java.sql.SQLException;

import core.jdbc.mysql.ConnectionPoolManager;
import core.jdbc.mysql.DBQuery;


public class TestDao extends DBQuery {
	public TestDao() throws SQLException, IOException {
		super("");
	}

	public void name() throws SQLException, InterruptedException {
		String jdbcDsn = null;
		ConnectionPoolManager.instance().getConnection(jdbcDsn);
	}
}
