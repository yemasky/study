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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.logging.Logger;

/**
 * @author CooC
 *
 */
public class ConnectionPoolManager {
	private final Logger logger = Logger.getLogger("jdbc.db.mysql.ConnectionPoolManager");
	private Hashtable<String, ConnectionPool> pools = new Hashtable<String, ConnectionPool>();// 连接池
	
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
			if(pool.getMaxConnection() == pool.getUsedPool()) {
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
}
