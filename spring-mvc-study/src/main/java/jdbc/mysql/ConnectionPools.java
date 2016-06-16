package jdbc.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

/**
 * @author CooC
 *
 */
public class ConnectionPools {
	private Connection connection = null;
	private int inUsed = 0; // ʹ�õ�������
	private ArrayList<Connection> freeConnectionsList = new ArrayList<Connection>();// ��������������
	private int minConnection; // ��С������
	private int maxConnection; // �������
	private String connectionName; // ���ӳ�����
	private String user; // �û���
	private String password; // ����
	private String url; // ���ݿ����ӵ�ַ
	private String driver; // ����
	public Timer timer; // ��ʱ

	/**
	  *
	  */
	public ConnectionPools() {
	}

	/**
	 * �������ӳ�
	 * 
	 * @param driver
	 * @param name
	 * @param URL
	 * @param user
	 * @param password
	 * @param maxConn
	 */
	public ConnectionPools(String connectionName, String driver, String url, String user, String password, int maxConnection) {
		this.connectionName = connectionName;
		this.driver = driver;
		this.url = url;
		this.user = user;
		this.password = password;
		this.maxConnection = maxConnection;
	}

	/**
	 * ���꣬�ͷ�����
	 * 
	 * @param con
	 */
	public synchronized void releaseConnection(Connection connection) {
		this.freeConnectionsList.add(connection);// ��ӵ��������ӵ�ĩβ
		this.inUsed--;
	}

	/**
	 * timeout ����timeout�õ�����
	 * 
	 * @param timeout
	 * @return
	 */
	public synchronized Connection getConnection(long timeout) {
		Connection connection = null;
		if (this.freeConnectionsList.size() > 0) {
			connection = (Connection) this.freeConnectionsList.get(0);
			if (connection == null)
				connection = getConnection(timeout); // �����������
		} else {
			connection = createConnection(); // �½�����
		}
		if (this.maxConnection == 0 || this.maxConnection < this.inUsed) {
			connection = null;// �ﵽ�������������ʱ���ܻ�������ˡ�
		}
		if (connection != null) {
			this.inUsed++;
		}
		return connection;
	}

	/**
	 *
	 * �����ӳ���õ�����
	 * 
	 * @return
	 */
	public synchronized Connection getConnection() {
		Connection connection = null;
		if (this.freeConnectionsList.size() > 0) {
			connection = (Connection) this.freeConnectionsList.get(0);
			this.freeConnectionsList.remove(0);// ������ӷ����ȥ�ˣ��ʹӿ���������ɾ��
			if (connection == null)
				connection = getConnection(); // �����������
		} else {
			connection = createConnection(); // �½�����
		}
		if (this.maxConnection == 0 || this.maxConnection < this.inUsed) {
			connection = null;// �ȴ� �����������ʱ
		}
		if (connection != null) {
			this.inUsed++;
			System.out.println("�õ���" + this.connectionName + "�������ӣ�����" + inUsed + "��������ʹ��!");
		}
		return connection;
	}

	/**
	 * �ͷ�ȫ������
	 *
	 */
	public synchronized void release() {
		Iterator<Connection> connections = this.freeConnectionsList.iterator();
		while (connections.hasNext()) {
			Connection connnection = (Connection) connections.next();
			try {
				connnection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		this.freeConnectionsList.clear();
	}

	/**
	 * ����������
	 * 
	 * @return
	 */
	private Connection createConnection() {
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("sorry can't find db driver!");
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("sorry can't create Connection!");
		}
		return connection;
	}

	/**
	 * ��ʱ������
	 */
	public synchronized void TimerEvent() {
		// ��ʱ��û��ʵ���Ժ����ϵ�
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
	 * @return the maxConn
	 */
	public int getMaxConnection() {
		return maxConnection;
	}

	/**
	 * @param maxConn
	 *            the maxConn to set
	 */
	public void setMaxConnection(int maxConnection) {
		this.maxConnection = maxConnection;
	}

	/**
	 * @return the minConn
	 */
	public int getMinConnection() {
		return minConnection;
	}

	/**
	 * @param minConn
	 *            the minConn to set
	 */
	public void setMinConnection(int minConn) {
		this.minConnection = minConn;
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
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
}