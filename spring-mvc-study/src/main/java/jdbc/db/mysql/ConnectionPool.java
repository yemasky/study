/***********************************************************************  
 *  
 *   @package：jdbc.db.mysql,@class-name：ConnectionPool.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package jdbc.db.mysql;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.PrintWriter;

/**
 * @author CooC
 * @email  yemasky@msn.com
 * @QQ     6796707
 *
 */
public class ConnectionPool implements DataSource {
	private final Logger logger = Logger.getLogger("jdbc.db.mysql.ConnectionPool");
	private final String dirverClassName = "com.mysql.jdbc.Driver";
	private Config config = null;
	// 连接池
	private static Map<String, LinkedList<Connection>> pool = new HashMap<String, LinkedList<Connection>>();
	private static Map<String, Integer> usedPool = new HashMap<String, Integer>();
	// 当前使用connection
	private final Map<String, ThreadLocal<Connection>> threadConnection = new HashMap<String, ThreadLocal<Connection>>();

	public ConnectionPool() {
	}

	public ConnectionPool(Config config) {
		try {
			Class.forName(dirverClassName);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "找不到驱动类.", e);
		}
		this.config = config;
	}

	public void init() throws SQLException {
		if (pool.get(config.getConnectionName()) == null) {
			pool.put(config.getConnectionName(), new LinkedList<Connection>());
			usedPool.put(config.getConnectionName(), 0);
			threadConnection.put(config.getConnectionName(), new ThreadLocal<Connection>());
		}
		if (pool.get(config.getConnectionName()).size() == 0 && usedPool.get(config.getConnectionName()) == 0) {
			Connection connection = null;
			for (int i = 0; i < config.getMinConnection(); i++) {
				connection = createConnection();
				pool.get(config.getConnectionName()).addLast(connection);
			}
			//logger.info("连接池初始化.");
		}
	}

	/**
	 * 获取一个数据库连接
	 * 
	 * @return 一个数据库连接
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		synchronized (pool.get(config.getConnectionName())) {
			logger.info("threadConnection.get: " + config.getConnectionName());
			Connection connection = threadConnection.get(config.getConnectionName()).get();
			if (connection != null)
				return connection;
			logger.info("new threadConnection.get: " + config.getConnectionName());
			if (pool.get(config.getConnectionName()).size() > 0) {
				setUsedPool(getUsedPool() + 1);
				logger.info("used: " + config.getConnectionName() + " -> " + getUsedPool());
				connection = pool.get(config.getConnectionName()).removeFirst();
				threadConnection.get(config.getConnectionName()).set(connection);
				return connection;
			} else {
				if (getUsedPool() == config.getMaxConnection()) {
					return null;
				}
				connection = createConnection();
				threadConnection.get(config.getConnectionName()).set(connection);
				setUsedPool(getUsedPool() + 1);
				return connection;
			}
		}
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return (Connection) DriverManager.getConnection(config.getDbDsn(), config.getDbUsername(), config.getDbPassword());
	}

	public void threadConnectionStatus() {
	}

	// 释放连接资源
	public void release() {
		if (threadConnection.get(config.getConnectionName()).get() != null)
			freeConnection(threadConnection.get(config.getConnectionName()).get());
	}

	/**
	 * 连接归池
	 * 
	 * @param connection
	 */
	public void freeConnection(Connection connection) {
		pool.get(config.getConnectionName()).addLast(connection);
		setUsedPool(getUsedPool() - 1);
		logger.info("used:" + config.getConnectionName() + " -> " + getUsedPool());
	}
	// 释放空闲连接资源 保留min的连接资源在连接池
	public synchronized void releaseFreeConnection() throws SQLException {
		int size = pool.get(config.getConnectionName()).size();
		int min = config.getMinConnection();
		if (size > min) {
			for(int i = 0; i < (size - min); i++) {
				this.closeConnection(pool.get(config.getConnectionName()).removeFirst());
			}
		}
	}
	
	public void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}

	private Connection createConnection() throws SQLException {
		return (Connection) DriverManager.getConnection(config.getDbDsn());
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	public void setLoginTimeout(int seconds) throws SQLException {
	}

	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		throw new SQLFeatureNotSupportedException();
	}

	public int getUsedPool() {
		return usedPool.get(config.getConnectionName());
	}

	public void setUsedPool(int used) {
		usedPool.put(config.getConnectionName(), used);
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}