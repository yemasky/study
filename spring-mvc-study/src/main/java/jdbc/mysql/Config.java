/***********************************************************************  
 *  
 *   @package��jdbc.mysql,@class-name��Config.java  
 *   
 *   �ܵ����ɵı������κι�˾����ˣ�δ����Ȩ�������Կ�����   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package jdbc.mysql;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;

/**
 * @author YEMASKY
 * @param
 * @return
 * @throws Exception
 */
public class Config {
	private String type = ""; // ���ݿ�����
	private String connectionName = ""; // ���ӳ�����
	private String driver = ""; // ���ݿ�����
	private String url = ""; // ���ݿ�url
	private String username = ""; // �û���
	private String password = ""; // ����
	private int maxConnection = 300; // ���������

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
