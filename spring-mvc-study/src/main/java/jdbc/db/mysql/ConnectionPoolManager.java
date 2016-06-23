/***********************************************************************  
 *  
 *   @package：jdbc.db.mysql,@class-name：ConnectionPoolManager.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package jdbc.db.mysql;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author CooC
 *
 */
public class ConnectionPoolManager {
	private final Logger logger = Logger.getLogger("jdbc.db.mysql.ConnectionPoolManager");
	private Hashtable<String, ConnectionPool> pools = new Hashtable<String, ConnectionPool>();// 连接池
	private static Map<String, Config> drivers = new HashMap<String, Config>();// 驱动信息

	/**
	 * 创建连接池
	 * 
	 * @param
	 */
	public void createPools(Config config) {
		ConnectionPool pool = new ConnectionPool();
		pool.setDbUrl(config.getUrl());
		pool.setDbUsername(config.getUsername());
		pool.setDbPassword(config.getPassword());
		pool.setMaxConnection(config.getMaxConnection());
		pools.put(config.getConnectionName(), pool);
	}

	/**
	 * 得到一个连接根据连接池的名字name
	 * 
	 * @param name
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String connectionName) throws SQLException {
		ConnectionPool pool = (ConnectionPool) pools.get(connectionName);// 从名字中获取连接池
		Connection connection = pool.getConnection();// 从选定的连接池中获得连接
		if (connection != null) {
			logger.info("得到连接.");
		} else {
			if (pool.getMaxConnection() == pool.getUsedPool()) {
				throw new SQLException("没有取到连接.连接已满.");
			} else {
				throw new SQLException("没有取到连接.连接异常.");
			}
		}
		return connection;
	}

	/**
	 * 释放连接
	 * 
	 * @param connectionName
	 * @param Connection
	 *            connection
	 */
	public void releaseConnection(String connectionName, Connection connection) {
		ConnectionPool pool = (ConnectionPool) pools.get(connectionName);// 根据关键名字得到连接池
		if (pool != null)
			pool.freeConnection(connection);// 释放连接
	}

	public Map<String, Config> loadDrivers() throws FileNotFoundException {
		drivers = null;
		try {

			/*
			 * List<?> pools = null; Element pool = null; Iterator<?> allPool =
			 * pools.iterator(); while (allPool.hasNext()) { pool = (Element)
			 * allPool.next(); Config dscBean = new Config();
			 * dscBean.setType(pool.getAttribute("type"));
			 * dscBean.setConnectionName(pool.getAttribute("name"));
			 * System.out.println(dscBean.getConnectionName());
			 * dscBean.setDriver(pool.getAttribute("driver"));
			 * dscBean.setUrl(pool.getAttribute("url"));
			 * dscBean.setUsername(pool.getAttribute("username"));
			 * dscBean.setPassword(pool.getAttribute("password"));
			 * dscBean.setMaxConnection(Integer.parseInt(pool.getAttribute(
			 * "maxconn"))); drivers.add(dscBean); }
			 */

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InputStream ferr = this.getClass().getClassLoader().getResourceAsStream("config/jdbc.db.properties");
		Properties properties = new Properties();

		try {
			properties.load(ferr);
			ferr.close();
			Set<Object> set = properties.keySet();
			Iterator<Object> it = set.iterator();
			String key = "";
			String value = "";
			Config config = null;
			while (it.hasNext()) {
				key = (String) it.next();
				value = properties.getProperty(key);
				logger.info("loading config: key->" + key + ", value->" + value);
				String[] keyArray = key.split(".");
				if (drivers.get(keyArray[1]) != null) {
					config = drivers.get(keyArray[1]);
				} else {
					config = new Config();
					config.setConnectionName(keyArray[1]);
				}
				if (keyArray[2] == "url") {
					config.setUrl(value);
				}
				if (keyArray[2] == "username") {
					config.setUsername(value);
				}
				if (keyArray[2] == "password") {
					config.setPassword(value);
				}
				if (keyArray[2] == "maxConnection") {
					config.setMaxConnection(Integer.parseInt(value));
				}
				logger.info(config.getConnectionName());
				drivers.put(keyArray[1], config);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drivers;

	}

}
