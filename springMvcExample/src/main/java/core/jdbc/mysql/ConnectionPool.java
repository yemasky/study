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

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Enumeration;
import java.util.HashMap;
//import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author CooC
 * @email yemasky@msn.com
 * @QQ 6796707
 *
 */
public class ConnectionPool implements DataSource {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String dirverClassName = "com.mysql.jdbc.Driver";
	private Config config = null;
	// 连接池
	//private static Map<String, LinkedList<Connection>> pool = new HashMap<String, LinkedList<Connection>>();
	private static Map<String, Vector<PooledConnection>> pool = new HashMap<>(); // 存放连接池中数据库连接的向量 
	private static Map<String, Integer> usedPool = new HashMap<String, Integer>();

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
			//pool.put(config.getConnectionName(), new LinkedList<Connection>());
			pool.put(config.getConnectionName(), new Vector<PooledConnection>());
			usedPool.put(config.getConnectionName(), 0);
		}
		if (pool.get(config.getConnectionName()).size() == 0 && usedPool.get(config.getConnectionName()) == 0) {
			// 把连接放进连接池
			for (int i = 0; i < config.getMinConnection(); i++) {
				Connection connection = this.createConnection();
				//pool.get(config.getConnectionName()).add(connection);
				pool.get(config.getConnectionName()).addElement(new PooledConnection(connection));
			}
		}
	}

	/**
	 * 获取一个数据库连接
	 * 
	 * @return 一个数据库连接
	 * @throws SQLException
	 */
	@Override
	public Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
		synchronized (pool.get(config.getConnectionName())) {
			// 当前线程
			Connection connection = null;
			PooledConnection pConn = null;
			// 从pool取得连接
			if (pool.get(config.getConnectionName()).size() > 0) {
				//connection = pool.get(config.getConnectionName()).removeFirst();
				// 获得连接池向量中所有的对象
				Enumeration<PooledConnection> enumerate = pool.get(config.getConnectionName()).elements();
				// 遍历所有的对象，看是否有可用的连接
				while (enumerate.hasMoreElements()) {
					pConn = (PooledConnection) enumerate.nextElement();
					if (!pConn.isBusy()) {
						// 如果此对象不忙，则获得它的数据库连接并把它设为忙
						connection = pConn.getConnection();
						if(connection.isValid(1)) {
							pConn.setBusy(true);
							break; // 己经找到一个可用的连接，退出
						} else {
							connection.close();
							pool.get(config.getConnectionName()).removeElement(pConn);
						}
					}
				}
				
				//connection = connectionLinked.getKey();
				//pool.get(config.getConnectionName()).remove(connection);
				if(connection != null && connection.isValid(1)) {
					setUsedPool(getUsedPool()+1);
					System.out.println("得到连接:"+connection.hashCode());
					return connection;
				}
				
			}
			// 如果使用达到最大连接返回null
			if (pool.get(config.getConnectionName()).size() >= config.getMaxConnection()) {
				return null;
			}
			// 否则创建连接并加到连接池
			connection = this.createConnection();
			pConn = new PooledConnection(connection);
			pConn.setBusy(true);
			pool.get(config.getConnectionName()).addElement(pConn);
			if(connection != null) {
				setUsedPool(getUsedPool()+1);
				System.out.println("新连接:"+connection.hashCode());
				return connection;
			}
			// 把当前的连接放到当前的线程
			// 使用的连接++
			return null;			
		}
	}

	// 得到mySql连接
	private Connection createConnection() {
		DriverManager.setLoginTimeout(1);
		try {
			logger.info("创建mySql连接:" + config.getConnectionName());
			return DriverManager.getConnection(config.getDbDsn());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("error createConnection:" + config.getDbDsn() + ":" + config.getConnectionName(), e);
		}
		return null;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return (Connection) DriverManager.getConnection(config.getDbDsn(), config.getDbUsername(),
				config.getDbPassword());
	}

	// 清理失效的连接
	public synchronized void clearLostedConnect() throws SQLException {
		// 每次运行5个连接
		int size = pool.get(config.getConnectionName()).size();
		int inSize = size > 5 ? 5 : size;
		int j = 0;
		int maxI = size > 100 ? 100 : size;
		for (int i = 0; i < maxI; i++) {
			//Connection connection = pool.get(config.getConnectionName()).get(i);
			PooledConnection pConn = findUsedPooledConnection();
			Connection connection = pConn.getConnection();
			if (!connection.isValid(1)) {
				j++;
				pConn.getConnection().close();
				pool.get(config.getConnectionName()).removeElement(pConn);
				//pool.get(config.getConnectionName()).remove(i);
				if (getUsedPool() > 0)
					setUsedPool(getUsedPool()-1);
				if(j == inSize) break;
			}
		}
	}

	private PooledConnection findUsedPooledConnection() throws SQLException {
		Connection conn = null;
		PooledConnection pConn = null;
		// 获得连接池向量中所有的对象
		Enumeration<PooledConnection> enumerate = pool.get(config.getConnectionName()).elements();
		// 遍历所有的对象，看是否有可用的连接
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			// 先找到连接池中的要返回的连接对象
			if (conn == pConn.getConnection()) {
				// 找到了 , 设置此连接为空闲状态
				pConn.setBusy(false);
				break;
			}
		}
		return pConn;// 返回找到到的可用连接
	}
	// 释放连接
	public synchronized void freeConnection(Connection connection) throws SQLException {
		System.out.println("准备释放连接:"+connection.hashCode());
		//pool.get(config.getConnectionName()).add(connection);
		if (getUsedPool() > 0)
			setUsedPool(getUsedPool()-1);
		logger.info("释放连接 ："+connection.hashCode()+",已使用连接数:" + getUsedPool() + "; 剩余连接：" + pool.get(config.getConnectionName()).size());
		PooledConnection pConn = null;
		// 获得连接池向量中所有的对象
		Enumeration<PooledConnection> enumerate = pool.get(config.getConnectionName()).elements();
		// 遍历所有的对象，看是否有可用的连接
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			// 先找到连接池中的要返回的连接对象
			if (connection == pConn.getConnection()) {
				// 找到了 , 设置此连接为空闲状态
				pConn.setBusy(false);
				//break;
				if (getUsedPool() > 0)
					setUsedPool(getUsedPool()-1);
				logger.info("释放连接 ："+connection.hashCode()+",已使用连接数:" + getUsedPool() + "; 剩余连接：" + pool.get(config.getConnectionName()).size());
				
			}
		}
		/*enumerate = pool.get(config.getConnectionName()).elements();
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			System.out.println(pConn.getConnection().hashCode() + "" + pConn.isBusy());
		}*/
		
	}

	public void closeConnection(Connection connection) throws SQLException {
		connection.close();
	}

	public int getUsedPool() {
		return usedPool.get(config.getConnectionName());
	}

	public void setUsedPool(int num) {
		usedPool.put(config.getConnectionName(), num);
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * 内部使用的用于保存连接池中连接对象的类 此类中有两个成员，一个是数据库的连接，另一个是指示此连接是否 正在使用的标志。
	 */

	class PooledConnection {
		Connection connection = null;// 数据库连接
		boolean busy = false; // 此连接是否正在使用的标志，默认没有正在使用

		// 构造函数，根据一个 Connection 构告一个 PooledConnection 对象
		public PooledConnection(Connection connection) {
			this.connection = connection;
		}

		// 返回此对象中的连接
		public Connection getConnection() {
			return connection;
		}

		// 设置此对象的，连接
		public void setConnection(Connection connection) {
			this.connection = connection;
		}

		// 获得对象连接是否忙
		public boolean isBusy() {
			return busy;
		}

		// 设置对象的连接正在忙
		public void setBusy(boolean busy) {
			this.busy = busy;
		}
	}

}