/***********************************************************************  
 *  
 *   @package：jdbc.mysql,@class-name：Config.java  
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
public class Config {
	private String type = ""; // 数据库类型
	private String connectionName = "default_db"; // 连接池名字
	private String driver = ""; // 数据库驱动
	private String url = ""; // 数据库url
	private String username = ""; // 用户名
	private String password = ""; // 密码
	private int maxConnection = 300; // 最大连接数

	public Config() {
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
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
