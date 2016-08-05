import java.io.IOException;
import java.sql.SQLException;

import jdbc.db.mysql.DBQuery;

public class TestDao extends DBQuery {
	public TestDao() throws SQLException, IOException {
		super();
	}
	private String table_name = "test";
	private String primary_key = "id";
	private String field = "*";
	
	public void name() throws SQLException, InterruptedException {
		String jdbcDsn = null;
		this.getConnection(jdbcDsn);
	}
}
