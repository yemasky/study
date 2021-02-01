package core.jdbc.mysql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

public class DbcpPool {
	private final String dirverClassName = "com.mysql.cj.jdbc.Driver";
	private Config config = null;
	/**
	 * 数据库连接池（dbcp连接池）对象引用
	 */
	private DataSource dbcpDataSource;

	public DbcpPool(Config config) throws SQLException {
		try {
			Class.forName(dirverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("找不到驱动类.");
		}
		this.config = config;
		init();
	}
	
	public Connection getConnection() throws SQLException {
		System.out.println("NumActive: " + ((BasicDataSource) dbcpDataSource).getNumActive());
		Connection connection = dbcpDataSource.getConnection();
		return connection;
	}

	public void init() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(dirverClassName);
		dataSource.setUsername(config.getDbUsername());
		dataSource.setPassword(config.getDbPassword());
		dataSource.setUrl(config.getDbDsn()+"&serverTimezone=GMT");
		dataSource.setInitialSize(config.getMinConnection());//初始连接数
		dataSource.setMaxTotal(config.getMaxConnection());//最大连接数
		dataSource.setMaxIdle(10);//最大空闲连接数
		dataSource.setMaxWaitMillis(5000);//5秒超时
		dataSource.setMinIdle(1);//最小空闲连接数
		dbcpDataSource = dataSource;
	}

	public void close(Connection connection) throws SQLException {
		connection.close();
	}

}
