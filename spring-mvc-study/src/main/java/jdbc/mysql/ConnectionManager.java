package jdbc.mysql;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;

//import com.chunkyo.db.ParseDSConfig;
//import com.chunkyo.db.DSConfigBean;
/**
 * @author chenyanlin
 *
 */
public class ConnectionManager {
	static private ConnectionManager instance;// 唯一数据库连接池管理实例类
	static private int clients; // 客户连接数
	private Vector<Config> drivers = new Vector<Config>();// 驱动信息
	private Hashtable<String, ConnectionPools> pools = new Hashtable<String, ConnectionPools>();// 连接池

	/**
	 * 实例化管理类
	 */
	public ConnectionManager() {
		this.init();
	}

	/**
	 * 得到唯一实例管理类
	 * 
	 * @return
	 */
	static synchronized public ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;

	}

	/**
	 * 释放连接
	 * 
	 * @param connectionName
	 * @param connection
	 *            ConnectionPoolDataSource
	 */
	public void releaseConnection(String connectionName, Connection connection) {
		ConnectionPools pool = (ConnectionPools) pools.get(connectionName);// 根据关键名字得到连接池
		if (pool != null)
			pool.releaseConnection(connection);// 释放连接
	}

	/**
	 * 得到一个连接根据连接池的名字name
	 * 
	 * @param name
	 * @return
	 */
	public Connection getConnection(String connectionName) {
		ConnectionPools pool = null;
		Connection connection = null;
		pool = (ConnectionPools) pools.get(connectionName);// 从名字中获取连接池
		connection = pool.getConnection();// 从选定的连接池中获得连接
		if (connection != null)
			System.out.println("得到连接...");
		return connection;
	}

	/**
	 * 得到一个连接，根据连接池的名字和等待时间
	 * 
	 * @param name
	 * @param time
	 * @return
	 */
	public Connection getConnection(String connectionName, long timeout) {
		ConnectionPools pool = null;
		Connection connection = null;
		pool = (ConnectionPools) pools.get(connectionName);// 从名字中获取连接池
		connection = pool.getConnection(timeout);// 从选定的连接池中获得连接
		System.out.println("得到连接...");
		return connection;
	}

	/**
	 * 释放所有连接
	 */
	public synchronized void release() {
		Enumeration<ConnectionPools> allpools = pools.elements();
		while (allpools.hasMoreElements()) {
			ConnectionPools pool = (ConnectionPools) allpools.nextElement();
			if (pool != null)
				pool.release();
		}
		pools.clear();
	}

	/**
	 * 创建连接池
	 * 
	 * @param props
	 */
	private void createPools(Config config) {
		ConnectionPools pool = new ConnectionPools();
		pool.setConnectionName(config.getConnectionName());
		pool.setDriver(config.getDriver());
		pool.setUrl(config.getUrl());
		pool.setUser(config.getUsername());
		pool.setPassword(config.getPassword());
		pool.setMaxConnection(config.getMaxConnection());
		pools.put(config.getConnectionName(), pool);
		System.out.println("pool:" + config.getMaxConnection());
	}

	/**
	 * 初始化连接池的参数
	 */
	private void init() {
		// 加载驱动程序
		this.loadDrivers();
		// 创建连接池
		Iterator<Config> driver = drivers.iterator();
		while (driver.hasNext()) {
			this.createPools((Config) driver.next());
			System.out.println("创建连接池。。。");

		}
		System.out.println("创建连接池完毕。。。");
	}

	/**
	 * 加载驱动程序
	 * 
	 * @param props
	 * @return 
	 */
	private Vector<Config> loadDrivers() {
		drivers = null;
		try {
	
			List<?> pools = null;
			Element pool = null;
			Iterator<?> allPool = pools.iterator();
			while (allPool.hasNext()) {
				pool = (Element) allPool.next();
				Config dscBean = new Config();
				dscBean.setType(pool.getAttribute("type"));
				dscBean.setConnectionName(pool.getAttribute("name"));
				System.out.println(dscBean.getConnectionName());
				dscBean.setDriver(pool.getAttribute("driver"));
				dscBean.setUrl(pool.getAttribute("url"));
				dscBean.setUsername(pool.getAttribute("username"));
				dscBean.setPassword(pool.getAttribute("password"));
				dscBean.setMaxConnection(Integer.parseInt(pool.getAttribute("maxconn")));
				drivers.add(dscBean);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		return drivers;
		// 读取数据库配置文件
		//drivers = config.readConfigInfo();
		//System.out.println("加载驱动程序。。。");
	}
}