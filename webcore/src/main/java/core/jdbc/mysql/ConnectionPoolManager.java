/***********************************************************************  
 *  
 *   @package：core.jdbc.mysql,@class-name：ConnectionPoolManager.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package core.jdbc.mysql;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author CooC
 * @email  yemasky@msn.com
 * @QQ     6796707
 *
 */
public class ConnectionPoolManager {
	private final Logger logger = Logger.getLogger("jdbc.db.mysql.ConnectionPoolManager");
	private static Hashtable<String, ConnectionPool> pools = new Hashtable<String, ConnectionPool>();// 连接池
	private static Map<String, HashMap<String, HashMap<String, Config>>> drivers = new HashMap<String, HashMap<String, HashMap<String, Config>>>();// 驱动信息
	private static long time = 0;
	private long timeout = 1000;
	private static boolean is_init = false;
	// 当前使用jdbcDsn test.write.001 config
	private final Map<String, ThreadLocal<String>> threadJdbcDsn = new HashMap<String, ThreadLocal<String>>();
	private static Map<String, ConnectionPoolManager> instances = new HashMap<String, ConnectionPoolManager>();

	public static ConnectionPoolManager instance() throws SQLException {
		ConnectionPoolManager instance = null;
		if (instances.containsKey("instances")) {
			instance = instances.get("instances");
		} else {
			boolean success = true;
			try {
				instance = new ConnectionPoolManager();
			} catch (SQLException e) {
				e.printStackTrace();
				success = false;
			} catch (IOException e) {
				e.printStackTrace();
				success = false;
			}
			if(!success) {
				throw new SQLException("数据库连接出错，请检查数据库连接.");
			}
		}
		instances.put("instances", instance);
		return instance;
	}

	public ConnectionPoolManager() throws SQLException, IOException {
		this.init();
	}

	public Config getConfigByDsn(String jdbcDsn) throws SQLException {
		String[] keyArray = jdbcDsn.split("\\.");
		Config config = null;
		if (keyArray.length < 2) {
			throw new SQLException("jdbcDsn is:" + jdbcDsn);
		}
		if (drivers.containsKey(keyArray[0]) && drivers.get(keyArray[0]).containsKey(keyArray[1])) {
			if (keyArray.length == 3) {
				config = drivers.get(keyArray[0]).get(keyArray[1]).get(keyArray[2]);
			}
			if (keyArray.length == 2) {// 随机取得连接
				Set<String> keySet = drivers.get(keyArray[0]).get(keyArray[1]).keySet();
				String[] keys = keySet.toArray(new String[keySet.size()]);
				Random random = new Random();
				String randomKey = keys[random.nextInt(keys.length)];
				config = drivers.get(keyArray[0]).get(keyArray[1]).get(randomKey);
			}
		} 
		if(config == null){
			throw new SQLException("没有从drivers取到config.");
		}
		return config;
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
	}

	/**
	 * 得到一个连接根据连接池的名字name
	 * 
	 * @param jdbcDsn
	 * @return
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public Connection getConnection(String jdbcDsn) throws SQLException {
		Config config = this.getConfigByDsn(jdbcDsn);
		String connectionName = config.getConnectionName();
		ConnectionPool pool = pools.get(connectionName);// 从名字中获取连接池
		if(pool == null) {
			throw new SQLException("没有取到连接池: " + connectionName);
		}
		Connection connection = pool.getConnection();// 从选定的连接池中获得连接
		if (connection != null) {
		} else {
			if (config.getMaxConnection() == pool.getUsedPool()) {
				logger.warning("连接已满,等待0.1秒");
				if (time == 0)
					time = System.currentTimeMillis();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new SQLException("线程睡眠出错");
				}
				if ((System.currentTimeMillis() - time) >= timeout) {// 超时5秒
					time = 0;
					throw new SQLException("没有取到连接.连接已满.超时5秒");
				}
				return this.getConnection(jdbcDsn);
			} else {
				throw new SQLException("没有取到连接.连接异常.");
			}
		}
		if (threadJdbcDsn.containsKey(jdbcDsn)) {
			threadJdbcDsn.get(jdbcDsn).remove();
			threadJdbcDsn.get(jdbcDsn).set(connectionName);
		} else {
			threadJdbcDsn.put(jdbcDsn, new ThreadLocal<String>());
			threadJdbcDsn.get(jdbcDsn).set(connectionName);
		}
		return connection;
	}

	/**
	 * 释放连接
	 * 
	 * @param connectionName
	 * @param Connection
	 *            connection
	 * @throws SQLException
	 */
	public void releaseConnection(String jdbcDsn, Connection connection) throws SQLException {
		if (threadJdbcDsn.containsKey(jdbcDsn)) {
			String connectionName = threadJdbcDsn.get(jdbcDsn).get();
			ConnectionPool pool = (ConnectionPool) pools.get(connectionName);// 根据关键名字得到连接池
			if (pool != null)
				pool.freeConnection(connection);// 释放连接
		}
	}

	public void releaseConnection(String jdbcDsn) throws SQLException {
		if (threadJdbcDsn.containsKey(jdbcDsn)) {
			String connectionName = threadJdbcDsn.get(jdbcDsn).get();
			ConnectionPool pool = pools.get(connectionName);// 根据关键名字得到连接池
			if (pool != null)
				pool.release();// 释放连接
		} else {
			logger.severe("无连接释放.");
		}
	}

	/**
	 * 加载数据库驱动
	 * 
	 * @throws IOException
	 */
	private Map<String, HashMap<String, HashMap<String, Config>>> loadDrivers() throws IOException {
		//logger.info("开始加载数据库驱动."); // 加载驱动程序
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
			if (keyArray.length > 3 && valueArray.length > 0) {
				config = new Config();
				config.setConnectionName(key);
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

				if (drivers.containsKey(keyArray[1])) {
					if (drivers.get(keyArray[1]).containsKey(keyArray[2])) {
						drivers.get(keyArray[1]).get(keyArray[2]).put(keyArray[3], config);
					} else {
						HashMap<String, Config> driverPollingMap = new HashMap<String, Config>();
						driverPollingMap.put(keyArray[3], config);
						drivers.get(keyArray[1]).put(keyArray[2], driverPollingMap);
					}
				} else {
					HashMap<String, Config> driverPollingMap = new HashMap<String, Config>();
					driverPollingMap.put(keyArray[3], config);
					HashMap<String, HashMap<String, Config>> driverMap = new HashMap<String, HashMap<String, Config>>();
					driverMap.put(keyArray[2], driverPollingMap);
					drivers.put(keyArray[1], driverMap);
				}
			} else {
				continue;
			}
		}
		return drivers;
	}

	/**
	 * 初始化连接池的参数
	 * 
	 * @throws SQLException
	 * @throws IOException
	 */
	public void init() throws SQLException, IOException {
		if(is_init) {
			return;
		}
		this.loadDrivers();
		for(String driverName : drivers.keySet()) {
			for(String excuteName : drivers.get(driverName).keySet()) {
				for(String pollingName : drivers.get(driverName).get(excuteName).keySet()) {
					Config config = drivers.get(driverName).get(excuteName).get(pollingName);
					this.createPools(config);//创建连接池
				}
			}
		}
		logger.info("创建连接池完毕.");
		is_init = true;
	}

	// 释放资源
	public void releaseConnection() throws SQLException {
		for(String driverName : drivers.keySet()) {
			for(String excuteName : drivers.get(driverName).keySet()) {
				for(String pollingName : drivers.get(driverName).get(excuteName).keySet()) {
					Config config = drivers.get(driverName).get(excuteName).get(pollingName);
					this.releaseConnection(config.getConnectionName());
				}
			}
		}
	}

	// 释放资源
	public void release(String connectionName) throws SQLException {
		ConnectionPool pool = pools.get(connectionName);
		pool.releaseFreeConnection();
		logger.info("释放资源完毕.");
	}

	// 释放所有空余资源
	public void releaseAllConnection() throws SQLException {
		for(String driverName : drivers.keySet()) {
			for(String excuteName : drivers.get(driverName).keySet()) {
				for(String pollingName : drivers.get(driverName).get(excuteName).keySet()) {
					Config config = drivers.get(driverName).get(excuteName).get(pollingName);
					ConnectionPool pool = pools.get(config.getConnectionName());
					pool.releaseFreeConnection();
					logger.info("释放空余资源:" + config.getConnectionName());
				}
			}
		}
		logger.info("释放空余资源完毕.");
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
