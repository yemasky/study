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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author CooC
 * @email  yemasky@msn.com
 * @QQ     6796707
 *
 */
public class ConnectionPoolManager {
	private final Logger logger = Logger.getLogger("core.jdbc.mysql.ConnectionPoolManager");
	private static Hashtable<String, ConnectionPool> pools = new Hashtable<String, ConnectionPool>();// 连接池
	private Map<String, HashMap<String, HashMap<String, Config>>> drivers = new HashMap<String, HashMap<String, HashMap<String, Config>>>();// 驱动信息
	private static long time = 0;
	private long timeout = 5000;
	private static boolean is_init = false;
	// 当前使用jdbcDsn test.write.001 config
	private static ConnectionPoolManager instances = null;

	public static ConnectionPoolManager instance() throws SQLException {
		if (instances == null) {
			instances = new ConnectionPoolManager();
		}
		return instances;
	}

	public ConnectionPoolManager() throws SQLException {
		this.init();
	}

	//分解dsn
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
			logger.info("得到 pool connection.");
		} else {
			if (config.getMaxConnection() >= pool.getUsedPool()) {
				logger.warning("连接已满,等待0.5秒");
				if (time == 0)
					time = System.currentTimeMillis();
				try {
					pool.clearLostedConnect();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new SQLException("线程睡眠出错");
				}
				if ((System.currentTimeMillis() - time) >= timeout) {// 超时5秒
					time = 0;
					logger.warning("没有取到连接.连接已满.超时5秒");
					//throw new SQLException("没有取到连接.连接已满.超时5秒");
				}
				return this.getConnection(jdbcDsn);
			} else {
				throw new SQLException("没有取到连接.连接异常.");
			}
		}

		return connection;
	}

	//释放连接
	public void freeConnection(String jdbcDsn, Connection connection) throws SQLException {
		Config config = this.getConfigByDsn(jdbcDsn);
		String connectionName = config.getConnectionName();
		ConnectionPool pool = pools.get(connectionName);// 从名字中获取连接池
		pool.freeConnection(connection);
	}
	/**
	 * 加载数据库驱动
	 * 
	 * @throws IOException
	 */
	private Map<String, HashMap<String, HashMap<String, Config>>> loadDrivers() throws IOException {
		//logger.info("开始加载数据库驱动."); // 加载驱动程序
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config/jdbc.mysql.properties");
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
						if (resuleArray[0].equals("user"))
							config.setDbUsername(resuleArray[1]);
						if (resuleArray[0].equals("password"))
							config.setDbPassword(resuleArray[1]);
						if (resuleArray[0].equals("maxConnection"))
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
	public void init() throws SQLException {
		if(is_init) {
			return;
		}
		try {
			this.loadDrivers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.log(Level.SEVERE, "读取数据库配置文件出错！", e);
			System.exit(0);//退出运行
		}
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

	public void releaseAllConnection(String jdbcDsn) throws SQLException {
		Config config = this.getConfigByDsn(jdbcDsn);
		String connectionName = config.getConnectionName();
		ConnectionPool pool = pools.get(connectionName);// 从名字中获取连接池
		if(pool == null) {
			throw new SQLException("没有取到连接池: " + connectionName);
		}
		pool.releaseAllConnection();
	}
	
	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
}
