/***********************************************************************  
 *  
 *   @package：jdbc.mysql,@class-name：SimpleDateSource.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package example.jdbc.mysql;

/**
 * @author YEMASKY
 * @param
 * @return
 * @throws Exception 
 */
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.DriverManager;
import java.io.PrintWriter;

/**
 * 一个简单的DataSource实现
 * 
 * @author leizhimin 2010-1-14 0:03:17
 */
public class SimpleDateSource implements DataSource {
	private static Log log = LogFactory.getLog(SimpleDateSource.class);
	private static final String dirverClassName = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://127.0.0.1:3306/testdb";
	private static final String user = "root";
	private static final String pswd = "leizhimin";
	// 连接池
	private static LinkedList<Connection> pool = (LinkedList<Connection>) Collections
			.synchronizedList(new LinkedList<Connection>());
	private static SimpleDateSource instance = new SimpleDateSource();

	static {
		try {
			Class.forName(dirverClassName);
		} catch (ClassNotFoundException e) {
			log.error("找不到驱动类！", e);
		}
	}

	private SimpleDateSource() {
	}

	/**
	 * 获取数据源单例
	 * 
	 * @return 数据源单例
	 */
	public SimpleDateSource instance() {
		if (instance == null)
			instance = new SimpleDateSource();
		return instance;
	}

	/**
	 * 获取一个数据库连接
	 * 
	 * @return 一个数据库连接
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		synchronized (pool) {
			if (pool.size() > 0)
				return pool.removeFirst();
			else
				return createConnection();
		}
	}

	/**
	 * 连接归池
	 * 
	 * @param conn
	 */
	public static void freeConnection(Connection conn) {
		pool.addLast(conn);
	}

	private Connection createConnection() throws SQLException {
		return DriverManager.getConnection(url, user, pswd);
	}

	public Connection getConnection(String username, String password) throws SQLException {
		return DriverManager.getConnection(url, username, password);
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

	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		throw new SQLFeatureNotSupportedException();
	}
}