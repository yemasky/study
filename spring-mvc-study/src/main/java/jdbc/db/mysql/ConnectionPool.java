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

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.DriverManager;
import java.io.PrintWriter;

/**
 * ConnectionPool实现
 * 
 * @author YEMASKY
 */
public class ConnectionPool implements DataSource {
	private static final Logger logger = Logger.getLogger("jdbc.db.mysql.ConnectionPool");
	private static final String dirverClassName = "com.mysql.jdbc.Driver";
	private static String dbUrl = "jdbc:mysql://127.0.0.1:3306/test?useSSL=false";
	private static String dbUsername = "root";
	private static String dbPassword = "root";
	private static int minConnection = 5; // 最小连接数
	private static int maxConnection = 1000; // 最大连接
	private static int timeout = 1000; // 连接时间
	// 连接池
	private static LinkedList<Connection> pool = new LinkedList<Connection>();
	private static int usedPool = 0;
	//当前使用connection
	private final ThreadLocal<Connection> threadConnection = new ThreadLocal<Connection>();  
	
	

	static {
		try {
			Class.forName(dirverClassName);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "找不到驱动类.", e);
		}
	}

	public ConnectionPool() {
	}

	/**
	 * 获取一个数据库连接
	 * 
	 * @return 一个数据库连接
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		synchronized (pool) {
			Connection connection = threadConnection.get();
			if(connection != null) return connection;
			if (pool.size() > 0) {
				setUsedPool(getUsedPool() + 1);
				logger.info("used:"+getUsedPool());
				connection = pool.removeFirst();
				threadConnection.set(connection);
				return connection;
				//return pool.removeFirst();
			} else {
				if(getUsedPool() == maxConnection) {
					return null;
				}
				return createConnection();
			}
		}
	}

	public void init() throws SQLException {
		if(pool.size() == 0 && usedPool == 0) {
			Connection connection = null;
			for (int i = 0; i < minConnection; i++) {
				connection = createConnection();
				pool.addLast(connection);
			}
			logger.info("连接池初始化完毕.");
		}
	}
	
	public void threadConnectionStatus() {
		//logger.info(threadConnection.hashCode() + ";ss");
	}
	
	//释放连接资源
	public void release() {
		if(threadConnection.get() != null)
			freeConnection(threadConnection.get());
	}

	/**
	 * 连接归池
	 * 
	 * @param connection
	 */
	public void freeConnection(Connection connection) {
		pool.addLast(connection);
		setUsedPool(getUsedPool() - 1);
		logger.info("used:"+getUsedPool());
	}

	public void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}

	private Connection createConnection() throws SQLException {
		return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return DriverManager.getConnection(dbUrl, username, password);
	}

	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	public void setLoginTimeout(int seconds) throws SQLException {
		timeout = seconds;
	}

	public int getLoginTimeout() throws SQLException {
		return timeout;
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

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbURL) {
		dbUrl = dbURL;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUserName) {
		dbUsername = dbUserName;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassWord) {
		dbPassword = dbPassWord;
	}

	public int getMinConnection() {
		return minConnection;
	}

	public void setMinConnection(int min) {
		minConnection = min;
	}

	public int getMaxConnection() {
		return maxConnection;
	}

	public void setMaxConnection(int max) {
		maxConnection = max;
	}

	public int getUsedPool() {
		return usedPool;
	}

	public static void setUsedPool(int used) {
		usedPool = used;
	}
}