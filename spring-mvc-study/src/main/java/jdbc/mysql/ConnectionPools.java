package jdbc.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

/**
 * @author CooC
 *
 */
public class ConnectionPools {
	private Connection connection = null;
	private int inUsed = 0; // 使用的连接数
	private ArrayList<Connection> freeConnectionsList = new ArrayList<Connection>();// 容器，空闲连接
	private int minConnection; // 最小连接数
	private int maxConnection; // 最大连接
	private String connectionName; // 连接池名字
	private String user; // 用户名
	private String password; // 密码
	private String url; // 数据库连接地址
	private String driver; // 驱动
	public Timer timer; // 定时

	/**
	  *
	  */
	public ConnectionPools() {
	}

	/**
	 * 创建连接池
	 * 
	 * @param driver
	 * @param name
	 * @param URL
	 * @param user
	 * @param password
	 * @param maxConn
	 */
	public ConnectionPools(String connectionName, String driver, String url, String user, String password, int maxConnection) {
		this.connectionName = connectionName;
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		this.maxConnection = maxConnection;
	}

	/**
	 * 用完，释放连接
	 * 
	 * @param con
	 */
	public synchronized void releaseConnection(Connection connection) {
		this.freeConnectionsList.add(connection);// 添加到空闲连接的末尾
		this.inUsed--;
	}

	/**
	 * timeout 根据timeout得到连接
	 * 
	 * @param timeout
	 * @return
	 */
	public synchronized Connection getConnection(long timeout) {
		Connection connection = null;
		if (this.freeConnectionsList.size() > 0) {
			connection = (Connection) this.freeConnectionsList.get(0);
			if (connection == null)
				connection = getConnection(timeout); // 继续获得连接
		} else {
			connection = createConnection(); // 新建连接
		}
		if (this.maxConnection == 0 || this.maxConnection < this.inUsed) {
			connection = null;// 达到最大连接数，暂时不能获得连接了。
		}
		if (connection != null) {
			this.inUsed++;
		}
		return connection;
	}

	/**
	 *
	 * 从连接池里得到连接
	 * 
	 * @return
	 */
	public synchronized Connection getConnection() {
		Connection connection = null;
		if (this.freeConnectionsList.size() > 0) {
			connection = (Connection) this.freeConnectionsList.get(0);
			this.freeConnectionsList.remove(0);// 如果连接分配出去了，就从空闲连接里删除
			if (connection == null)
				connection = getConnection(); // 继续获得连接
		} else {
			connection = createConnection(); // 新建连接
		}
		if (this.maxConnection == 0 || this.maxConnection < this.inUsed) {
			connection = null;// 等待 超过最大连接时
		}
		if (connection != null) {
			this.inUsed++;
			System.out.println("得到　" + this.connectionName + "　的连接，现有" + inUsed + "个连接在使用!");
		}
		return connection;
	}

	/**
	 * 释放全部连接
	 *
	 */
	public synchronized void release() {
		Iterator<Connection> connections = this.freeConnectionsList.iterator();
		while (connections.hasNext()) {
			Connection connnection = (Connection) connections.next();
			try {
				connnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		this.freeConnectionsList.clear();
	}

	/**
	 * 创建新连接
	 * 
	 * @return
	 */
	private Connection createConnection() {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("sorry can't find db driver!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("sorry can't create Connection!");
		}
		return connection;
	}

	/**
	 * 定时处理函数
	 */
	public synchronized void TimerEvent() {
		// 暂时还没有实现以后会加上的
	}

	/**
	 * @return the driver
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * @param driver
	 *            the driver to set
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * @return the maxConn
	 */
	public int getMaxConnection() {
		return maxConnection;
	}

	/**
	 * @param maxConn
	 *            the maxConn to set
	 */
	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	/**
	 * @return the minConn
	 */
	public int getMinConnection() {
		return minConnection;
	}

	/**
	 * @param minConn
	 *            the minConn to set
	 */
	public void setMinConnection(int minConn) {
		this.minConnection = minConn;
	}

	/**
	 * @return the name
	 */
	public String getConnectionName() {
		return connectionName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
}