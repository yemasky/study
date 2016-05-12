/** 2016年5月13日  user.java   **/
package entity;

/**
 * @author YEMASKY
 *
 */
public class Users {
	private int uid;
	private String username;
	private String password;
	
	public Users() {
		
	}
	/**
	 * @param uid
	 * @param username
	 * @param password
	 */
	public Users(int uid, String username, String password) {
		this.uid = uid;
		this.username = username;
		this.password = password;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
