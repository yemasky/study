/***********************************************************************  
 *  
 *   @package：jdbc.db.mysql,@class-name：Config.java  
 *   
 *   受到法律的保护，任何公司或个人，未经授权不得擅自拷贝。   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package core.jdbc.mysql;

/**
 * @author YEMASKY
 * @param
 * @return
 * @throws Exception
 */
public class Config {
	private String connectionName = "default"; // 连接池名字
	private String dbDsn = "jdbc:mysql://127.0.0.1:3306/test?user=root&password=root&useSSL=false"; // 数据库url
	private String dbUsername = "root"; // 用户名
	private String dbPassword = "root"; // 密码
	private int maxConnection = 300; // 最大连接数
	private int minConnection = 1; // 最小连接数

	public Config() {
	}

	/**
	 * @return the maxconnection
	 */
	public int getMaxConnection() {
		return maxConnection;
	}

	/**
	 * @param maxconnection
	 *            the maxconnection to set
	 */
	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	public int getMinConnection() {
		return minConnection;
	}

	public void setMinConnection(int minConnection) {
		this.minConnection = minConnection;
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

	public String getDbDsn() {
		return dbDsn;
	}

	public void setDbDsn(String dbDsn) {
		this.dbDsn = dbDsn;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	
}
