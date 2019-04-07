package core.jdbc.mysql;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

public class DbcpConnection {
	/**
	 * 数据库连接池（dbcp连接池）对象引用
	 */
	private static DataSource dbcpDataSource;
	/**
	 * 只执行一次
	 */
	static {
		Properties properties = new Properties();
		ClassLoader classLoader = DbcpPool.class.getClassLoader();
		InputStream resourceAsStream = classLoader.getResourceAsStream("config/dbcp.mysql.001.properties");
		try {
			System.out.println("得到连接");
			properties.load(resourceAsStream);
			dbcpDataSource = BasicDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
		
	public static Connection getConnection() throws SQLException {
		System.out.println("NumActive: " + ((BasicDataSource) dbcpDataSource).getNumActive());
		Connection connection = dbcpDataSource.getConnection();
		return connection;
	}

	public static void close(Connection connection) throws SQLException {
		connection.close();
	}

}
