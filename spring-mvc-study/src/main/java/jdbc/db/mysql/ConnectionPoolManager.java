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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	private static Map<String, HashMap<String, HashMap<String, Config>>> drivers = new HashMap<String, HashMap<String, HashMap<String, Config>>>();// 驱动信息
	//test.write.001 config
	public ConnectionPoolManager() {
	}

	/**
	 * 创建连接池
	 * 
	 * @param
	 * @throws SQLException
	 */
	public void createPools(Config config) throws SQLException {
		ConnectionPool pool = new ConnectionPool(config);
		pool.init();
		pools.put(config.getConnectionName(), pool);
		logger.info("创建连接池：" + config.getConnectionName());
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
			if (drivers.get(connectionName).getMaxConnection() == pool.getUsedPool()) {
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

	public void releaseConnection(String connectionName) {
		ConnectionPool pool = (ConnectionPool) pools.get(connectionName);// 根据关键名字得到连接池
		if (pool != null)
			pool.release();// 释放连接
	}

	private Map<String, Config> loadDrivers() throws FileNotFoundException {
		logger.info("开始加载数据库驱动."); // 加载驱动程序
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config/jdbc.db.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			inputStream.close();
			Set<Object> set = properties.keySet();
			Iterator<Object> it = set.iterator();
			String key = "";
			String value = "";
			Config config = null;
			while (it.hasNext()) {
				key = (String) it.next();
				value = properties.getProperty(key);
				logger.info("loading config: key->" + key + ", value->" + value);
				String[] keyArray = key.split("\\.");
				String[] valueArray = value.substring(value.indexOf("?") + 1).split("&");
				if (drivers.get(keyArray[1]) != null) {
					config = drivers.get(keyArray[1]).get(keyArray[2]).get(keyArray[3]);
				} else {
					config = new Config();
					config.setConnectionName(keyArray[1]);
				}
				if (keyArray.length > 0 && valueArray.length > 0) {
					config.setDbDsn(value);
					for (int i = 0; i < valueArray.length; i++) {
						String[] resuleArray = valueArray[i].split("=");
						if (resuleArray.length == 2) {
							if (resuleArray[0] == "user")
								config.setDbUsername(resuleArray[1]);
							if (resuleArray[0] == "password")
								config.setDbPassword(resuleArray[1]);
							if (resuleArray[0] == "maxConnection")
								config.setMaxConnection(Integer.parseInt(resuleArray[1]));
						}
					}
					HashMap<String, Config> pollingList =  new HashMap<String, Config>();
					if(driverPollingMap.containsKey(keyArray[2])) {
						driverPollingMap.get(keyArray[2]).put(keyArray[3], config);
					} else {	
						pollingList.put(keyArray[3], config);
						driverPollingMap.put(keyArray[2], pollingList);				
					}//test.write.001 config
					
					if(drivers.containsKey(keyArray[1])) {
						if(drivers.get(keyArray[1]).containsKey(keyArray[2])) {
							
						}
						HashMap<String, Config> driverPollingMap = new HashMap<String, Config>();
						driverPollingMap.put(keyArray[3], config);
						drivers.get(keyArray[1]).put(keyArray[2], driverPollingMap);
					} else {
					}
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return drivers;
	}

	/**
	 * 初始化连接池的参数
	 * 
	 * @throws FileNotFoundException
	 * @throws SQLException
	 */
	public void init() throws FileNotFoundException, SQLException {
		this.loadDrivers();
		Iterator<Entry<String, Config>> driver = drivers.entrySet().iterator();
		while (driver.hasNext()) {
			this.createPools((Config) driver.next().getValue());
		}
		logger.info("创建连接池完毕.");
	}

	// 释放资源
	public void release() {
		Iterator<Entry<String, Config>> driver = drivers.entrySet().iterator();
		while (driver.hasNext()) {
			this.releaseConnection(driver.next().getKey());
		}
		logger.info("释放连接池完毕.");
	}

	// 关闭资源
	public void close(String connectionName) throws SQLException {
		ConnectionPool pool = (ConnectionPool) pools.get(connectionName);
		pool.close();
		logger.info("关闭资源完毕.");
	}

	// 关闭所有空余资源
	public void close() throws SQLException {
		Iterator<Entry<String, Config>> driver = drivers.entrySet().iterator();
		while (driver.hasNext()) {
			ConnectionPool pool = (ConnectionPool) pools.get(driver.next().getKey());
			pool.close();
		}
		logger.info("关闭空余资源完毕.");
	}
}
