/** 2016年5月13日  user.java   **/
package entity;

import java.util.Date;

/**
 * @author YEMASKY
 *
 */
public class Students {
	private String sid;
	private String sname;
	private String gender;
	private Date birthday;
	public Students() {
		
	}
	
	/**
	 * @param sid
	 * @param sname
	 * @param gender
	 * @param birthday
	 */
	public Students(String sid, String sname, String gender, Date birthday) {
		super();
		this.sid = sid;
		this.sname = sname;
		this.gender = gender;
		this.birthday = birthday;
	}

	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	} 
	
	
}
