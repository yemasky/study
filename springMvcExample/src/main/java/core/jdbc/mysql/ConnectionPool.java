/***********************************************************************  
 *  
 *   @package：core.jdbc.mysql,@class-name：ConnectionPool.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package core.jdbc.mysql;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String dirverClassName = "com.mysql.jdbc.Driver";
	private Config config = null;
	// 连接池
	private static Map<String, LinkedList<Connection>> pool = new HashMap<String, LinkedList<Connection>>();
	private static Map<String, Integer> usedPool = new HashMap<String, Integer>();
	// 当前使用connection
	private Map<String, ThreadLocal<Connection>> threadConnection = new HashMap<String, ThreadLocal<Connection>>();

	public ConnectionPool() {
	}

	public ConnectionPool(Config config) throws SQLException {
		try {
			Class.forName(dirverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("找不到驱动类.");
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
			//把连接放进连接池
			for (int i = 0; i < config.getMinConnection(); i++) {
				connection = createConnection();
				if(connection != null) pool.get(config.getConnectionName()).addLast(connection);
			}
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
			logger.info("线程" + threadConnection.hashCode() + config.getConnectionName());
			//当前线程
			Connection connection = null;
			ThreadLocal<Connection> threadLocalConnection = threadConnection.get(config.getConnectionName());
			if(threadLocalConnection == null) {
				threadLocalConnection = new ThreadLocal<Connection>();
				//当前连接-当前线程
				threadConnection.put(config.getConnectionName(), threadLocalConnection);
			} else {
				//取出当前线程的连接
				connection = threadLocalConnection.get();
			}
			//当前连接未失效
			if (connection != null && !connection.isClosed() && connection.isValid(1)) {
				setUsedPool(getUsedPool() + 1);
				logger.info("当前连接 isValid->true"+ " ;共使用:" + getUsedPool());// + connection.isValid(1)
				return connection;
			} else {
				//并减掉使用的连接数量1
				logger.info("失效连接 : 1");
				if(getUsedPool() > 0) setUsedPool(getUsedPool() - 1);
			}
			//如果已失效从pool取得连接
			if (pool.get(config.getConnectionName()).size() > 0) {
				connection = pool.get(config.getConnectionName()).removeFirst();
				boolean isValid = connection.isValid(1);
				logger.info("isValid:" + isValid);
				if(isValid) {
					setUsedPool(getUsedPool() + 1);
					logger.info("使用连接: " + config.getConnectionName() + " -> " + getUsedPool() + " ;共使用:" + getUsedPool());
					threadConnection.get(config.getConnectionName()).set(connection);
					return connection;
				}
			}
			//如果使用达到最大连接返回null
			if (getUsedPool() >= config.getMaxConnection()) {
				return null;
			}
			logger.info("新连接: " + config.getConnectionName());
			//否则创建连接并加到连接池
			connection = createConnection();
			pool.get(config.getConnectionName()).addLast(connection);
			//把当前的连接放到当前的线程
			threadConnection.get(config.getConnectionName()).set(connection);
			//使用的连接++
			setUsedPool(getUsedPool() + 1);
			return connection;
		}
	}

	//清理失效的连接
	public synchronized void clearLostedConnect() throws SQLException {
		//每次运行5个连接
		int size = pool.get(config.getConnectionName()).size();
		int inSize = size > 5 ? 5 : size;
		for(int i = 0; i < inSize; i++) {
			Connection connection = pool.get(config.getConnectionName()).get(i);
			boolean isValid = connection.isValid(1);
			System.out.println("connection.isValid："+isValid);
			if(!isValid) {
				pool.get(config.getConnectionName()).remove(i);
				if(getUsedPool() > 0) setUsedPool(getUsedPool() - 1);
			}
		}
	}
	
	public Connection getConnection(String username, String password) throws SQLException {
		return (Connection) DriverManager.getConnection(config.getDbDsn(), config.getDbUsername(), config.getDbPassword());
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
	
	public synchronized void releaseAllConnection() throws SQLException {
		int size = pool.get(config.getConnectionName()).size();
		for(int i = 0; i < size; i++) {
			this.closeConnection(pool.get(config.getConnectionName()).removeFirst());
		}
	}
	//释放连接
	public synchronized void freeConnection(Connection connection) throws SQLException {
		pool.get(config.getConnectionName()).addLast(connection);
		if(getUsedPool() > 0) setUsedPool(getUsedPool() - 1);
	}
	
	public void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}

	//得到mySql连接
	private Connection createConnection() {
		Connection thisConnection = null;
		try {
			DriverManager.setLoginTimeout(1);
			thisConnection = DriverManager.getConnection(config.getDbDsn());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("数据库连接失败：" + config.getDbDsn(), e);
			e.printStackTrace();
		}
		return thisConnection;
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

	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
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