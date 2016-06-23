/***********************************************************************  
 *  
 *   @package��jdbc.pool,@class-name��ConnectionPool.java  
 *   
 *   �ܵ����ɵı������κι�˾����ˣ�δ����Ȩ�������Կ�����   
 *   @copyright       Copyright:   2016-2018     
 *   @creator         YEMASKY
 *   @create-time     2016 {time}
 *   @revision        Id: 1.0    
 ***********************************************************************/
package example.jdbc.pool;

/**
 * @author YEMASKY
 * @param
 * @return
 * @throws Exception 
 */
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Vector;

import example.jdbc.pool.ConnectionPool.PooledConnection;

public class ConnectionPool {
	private String jdbcDriver = ""; // ���ݿ�����
	private String dbUrl = ""; // ���� URL
	private String dbUsername = ""; // ���ݿ��û���
	private String dbPassword = ""; // ���ݿ��û�����
	private String testTable = ""; // ���������Ƿ���õĲ��Ա�����Ĭ��û�в��Ա�
	private int initialConnections = 10; // ���ӳصĳ�ʼ��С
	private int incrementalConnections = 5;// ���ӳ��Զ����ӵĴ�С
	private int maxConnections = 50; // ���ӳ����Ĵ�С
	private Vector<PooledConnection> connections = null; // ������ӳ������ݿ����ӵ����� ,
															// ��ʼʱΪ null

	// ���д�ŵĶ���Ϊ PooledConnection ��

	/**
	 * ���캯��
	 *
	 * @param jdbcDriver
	 *            String JDBC �����മ
	 * @param dbUrl
	 *            String ���ݿ� URL
	 * @param dbUsername
	 *            String �������ݿ��û���
	 * @param dbPassword
	 *            String �������ݿ��û�������
	 *
	 */

	public ConnectionPool(String jdbcDriver, String dbUrl, String dbUsername, String dbPassword) {
		this.jdbcDriver = jdbcDriver;
		this.dbUrl = dbUrl;
		this.dbUsername = dbUsername;
		this.dbPassword = dbPassword;
	}

	/**
	 * 
	 * �������ӳصĳ�ʼ��С
	 *
	 * @return ��ʼ���ӳ��пɻ�õ���������
	 */
	public int getInitialConnections() {
		return this.initialConnections;
	}

	/**
	 * 
	 * �������ӳصĳ�ʼ��С
	 *
	 * 
	 * 
	 * @param �������ó�ʼ���ӳ������ӵ�����
	 * 
	 */
	public void setInitialConnections(int initialConnections) {
		this.initialConnections = initialConnections;
	}

	/**
	 * 
	 * �������ӳ��Զ����ӵĴ�С ��
	 *
	 * @return ���ӳ��Զ����ӵĴ�С
	 */
	public int getIncrementalConnections() {
		return this.incrementalConnections;
	}

	/**
	 * �������ӳ��Զ����ӵĴ�С
	 * 
	 * @param ���ӳ��Զ����ӵĴ�С
	 */
	public void setIncrementalConnections(int incrementalConnections) {
		this.incrementalConnections = incrementalConnections;
	}

	/**
	 * �������ӳ������Ŀ�����������
	 * 
	 * @return ���ӳ������Ŀ�����������
	 */

	public int getMaxConnections() {
		return this.maxConnections;
	}

	/**
	 * 
	 * �������ӳ��������õ���������
	 *
	 * 
	 * 
	 * @param �������ӳ��������õ���������ֵ
	 * 
	 */

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	/**
	 * 
	 * ��ȡ�������ݿ�������
	 *
	 * @return �������ݿ�������
	 */
	public String getTestTable() {
		return this.testTable;
	}

	/**
	 * ���ò��Ա������
	 * 
	 * @param testTable
	 *            String ���Ա������
	 */
	public void setTestTable(String testTable) {
		this.testTable = testTable;
	}

	/**
	 *
	 * 
	 * ����һ�����ݿ����ӳأ����ӳ��еĿ������ӵ������������Ա initialConnections �����õ�ֵ
	 */
	public synchronized void createPool() throws Exception {
		// ȷ�����ӳ�û�д���
		// ������ӳؼ��������ˣ��������ӵ����� connections ����Ϊ��
		if (connections != null) {
			return; // ��������������򷵻�
		}		// ʵ���� JDBC Driver ��ָ����������ʵ��
		Driver driver = (Driver) (Class.forName(this.jdbcDriver).newInstance());
		DriverManager.registerDriver(driver); // ע�� JDBC ��������
		// �����������ӵ����� , ��ʼʱ�� 0 ��Ԫ��
		connections = new Vector<PooledConnection>();
		// ���� initialConnections �����õ�ֵ���������ӡ�
		createConnections(this.initialConnections);
		System.out.println(" ���ݿ����ӳش����ɹ��� ");	}

	/**
	 * 
	 * ������ numConnections ָ����Ŀ�����ݿ����� , ������Щ����
	 * 
	 * ���� connections ������
	 *
	 * @param numConnections
	 *            Ҫ���������ݿ����ӵ���Ŀ
	 */
	private void createConnections(int numConnections) throws SQLException {
		// ѭ������ָ����Ŀ�����ݿ�����
		for (int x = 0; x < numConnections; x++) {
			// �Ƿ����ӳ��е����ݿ����ӵ����������ﵽ������ֵ�����Ա maxConnections
			// ָ������� maxConnections Ϊ 0 ��������ʾ��������û�����ơ�
			// ��������������ﵽ��󣬼��˳���
			if (this.maxConnections > 0 && this.connections.size() >= this.maxConnections) {
				break;
			}
			// add a new PooledConnection object to connections vector
			// ����һ�����ӵ����ӳ��У����� connections �У�
			try {
				connections.addElement(new PooledConnection(newConnection()));
			} catch (SQLException e) {
				System.out.println(" �������ݿ�����ʧ�ܣ� " + e.getMessage());
				throw new SQLException();
			}
			System.out.println(" ���ݿ����Ӽ����� ......");
		}
	}

	/**
	 * 
	 * ����һ���µ����ݿ����Ӳ�������
	 *
	 * @return ����һ���´��������ݿ�����
	 */
	private Connection newConnection() throws SQLException {
		// ����һ�����ݿ�����
		Connection conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		// ������ǵ�һ�δ������ݿ����ӣ���������ݿ⣬��ô����ݿ�����֧�ֵ�
		// ���ͻ�������Ŀ
		// connections.size()==0 ��ʾĿǰû�����Ӽ�������
		if (connections.size() == 0) {
			DatabaseMetaData metaData = conn.getMetaData();
			int driverMaxConnections = metaData.getMaxConnections();
			// ���ݿⷵ�ص� driverMaxConnections ��Ϊ 0 ����ʾ�����ݿ�û�����
			// �������ƣ������ݿ������������Ʋ�֪��
			// driverMaxConnections Ϊ���ص�һ����������ʾ�����ݿ�����ͻ����ӵ���Ŀ
			// ������ӳ������õ�������������������ݿ������������Ŀ , �������ӳص����
			// ������ĿΪ���ݿ�����������Ŀ
			if (driverMaxConnections > 0 && this.maxConnections > driverMaxConnections) {
				this.maxConnections = driverMaxConnections;
			}
		}
		return conn; // ���ش������µ����ݿ�����
	}

	/**
	 * 
	 * ͨ������ getFreeConnection() ��������һ�����õ����ݿ����� ,
	 * 
	 * �����ǰû�п��õ����ݿ����ӣ����Ҹ�������ݿ����Ӳ��ܴ�
	 * 
	 * ���������ӳش�С�����ƣ����˺����ȴ�һ���ٳ��Ի�ȡ��
	 *
	 * 
	 * 
	 * @return ����һ�����õ����ݿ����Ӷ���
	 * 
	 */

	public synchronized Connection getConnection() throws SQLException {
		// ȷ�����ӳؼ�������
		if (connections == null) {
			return null; // ���ӳػ�û�������򷵻� null
		}
		Connection conn = getFreeConnection(); // ���һ�����õ����ݿ�����
		// ���Ŀǰû�п���ʹ�õ����ӣ������е����Ӷ���ʹ����
		while (conn == null) {
			// ��һ������
			wait(250);
			conn = getFreeConnection(); // �������ԣ�ֱ����ÿ��õ����ӣ����
			// getFreeConnection() ���ص�Ϊ null
			// ���������һ�����Ӻ�Ҳ���ɻ�ÿ�������
		}
		return conn;// ���ػ�õĿ��õ�����
	}

	/**
	 * 
	 * �����������ӳ����� connections �з���һ�����õĵ����ݿ����ӣ����
	 * 
	 * ��ǰû�п��õ����ݿ����ӣ������������ incrementalConnections ����
	 * 
	 * ��ֵ�����������ݿ����ӣ����������ӳ��С�
	 * 
	 * ������������е������Զ���ʹ���У��򷵻� null
	 * 
	 * @return ����һ�����õ����ݿ�����
	 * 
	 */

	private Connection getFreeConnection() throws SQLException {
		// �����ӳ��л��һ�����õ����ݿ�����
		Connection conn = findFreeConnection();
		if (conn == null) {
			// ���Ŀǰ���ӳ���û�п��õ�����
			// ����һЩ����
			createConnections(incrementalConnections);
			// ���´ӳ��в����Ƿ��п�������
			conn = findFreeConnection();
			if (conn == null) {
				// ����������Ӻ��Ի�ò������õ����ӣ��򷵻� null
				return null;
			}
		}
		return conn;
	}

	/**
	 * 
	 * �������ӳ������е����ӣ�����һ�����õ����ݿ����ӣ�
	 * 
	 * ���û�п��õ����ӣ����� null
	 *
	 * 
	 * 
	 * @return ����һ�����õ����ݿ�����
	 * 
	 */

	private Connection findFreeConnection() throws SQLException {
		Connection conn = null;
		PooledConnection pConn = null;
		// ������ӳ����������еĶ���
		Enumeration<PooledConnection> enumerate = connections.elements();
		// �������еĶ��󣬿��Ƿ��п��õ�����
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			if (!pConn.isBusy()) {
				// ����˶���æ�������������ݿ����Ӳ�������Ϊæ
				conn = pConn.getConnection();
				pConn.setBusy(true);
				// ���Դ������Ƿ����
				if (!testConnection(conn)) {
					// ��������Ӳ��������ˣ��򴴽�һ���µ����ӣ�
					// ���滻�˲����õ����Ӷ����������ʧ�ܣ����� null
					try {
						conn = newConnection();
					} catch (SQLException e) {
						System.out.println(" �������ݿ�����ʧ�ܣ� " + e.getMessage());
						break;
					}
					pConn.setConnection(conn);
				}
				break; // �����ҵ�һ�����õ����ӣ��˳�
			}
		}
		return conn;// �����ҵ����Ŀ�������

	}

	/**
	 * 
	 * ����һ�������Ƿ���ã���������ã��ص��������� false
	 * 
	 * ������÷��� true
	 *
	 * 
	 * 
	 * @param conn
	 *            ��Ҫ���Ե����ݿ�����
	 * 
	 * @return ���� true ��ʾ�����ӿ��ã� false ��ʾ������
	 * 
	 */

	private boolean testConnection(Connection conn) {
		try {
			// �жϲ��Ա��Ƿ����
			if (testTable.equals("")) {
				// ������Ա�Ϊ�գ�����ʹ�ô����ӵ� setAutoCommit() ����
				// ���ж����ӷ���ã��˷���ֻ�ڲ������ݿ���ã���������� ,
				// �׳��쳣����ע�⣺ʹ�ò��Ա�ķ������ɿ�
				conn.setAutoCommit(true);
			} else {// �в��Ա��ʱ��ʹ�ò��Ա����
				// check if this connection is valid
				Statement stmt = conn.createStatement();
				stmt.execute("select count(*) from " + testTable);
			}
		} catch (SQLException e) {
			// �����׳��쳣�������Ӽ������ã��ر����������� false;
			closeConnection(conn);
			return false;
		}
		// ���ӿ��ã����� true
		return true;
	}
	/**
	 * 
	 * �˺�������һ�����ݿ����ӵ����ӳ��У����Ѵ�������Ϊ���С�
	 * 
	 * ����ʹ�����ӳػ�õ����ݿ����Ӿ�Ӧ�ڲ�ʹ�ô�����ʱ��������
	 *
	 * 
	 * 
	 * @param �践�ص����ӳ��е����Ӷ���
	 * 
	 */
	public void returnConnection(Connection conn) {
		// ȷ�����ӳش��ڣ��������û�д����������ڣ���ֱ�ӷ���
		if (connections == null) {
			System.out.println(" ���ӳز����ڣ��޷����ش����ӵ����ӳ��� !");
			return;
		}
		PooledConnection pConn = null;
		Enumeration<PooledConnection> enumerate = connections.elements();
		// �������ӳ��е��������ӣ��ҵ����Ҫ���ص����Ӷ���
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			// ���ҵ����ӳ��е�Ҫ���ص����Ӷ���
			if (conn == pConn.getConnection()) {
				// �ҵ��� , ���ô�����Ϊ����״̬
				pConn.setBusy(false);
				break;
			}
		}
	}

	/**
	 * 
	 * ˢ�����ӳ������е����Ӷ���
	 *
	 * 
	 * 
	 */

	public synchronized void refreshConnections() throws SQLException {
		// ȷ�����ӳؼ����´���
		if (connections == null) {
			System.out.println(" ���ӳز����ڣ��޷�ˢ�� !");
			return;
		}
		PooledConnection pConn = null;
		Enumeration<PooledConnection> enumerate = connections.elements();
		while (enumerate.hasMoreElements()) {
			// ���һ�����Ӷ���
			pConn = (PooledConnection) enumerate.nextElement();
			// �������æ��� 5 �� ,5 ���ֱ��ˢ��
			if (pConn.isBusy()) {
				wait(5000); // �� 5 ��
			}
			// �رմ����ӣ���һ���µ����Ӵ�������
			closeConnection(pConn.getConnection());
			pConn.setConnection(newConnection());
			pConn.setBusy(false);
		}
	}

	/**
	 * 
	 * �ر����ӳ������е����ӣ���������ӳء�
	 * 
	 */

	public synchronized void closeConnectionPool() throws SQLException {
		// ȷ�����ӳش��ڣ���������ڣ�����
		if (connections == null) {
			System.out.println(" ���ӳز����ڣ��޷��ر� !");
			return;
		}
		PooledConnection pConn = null;
		Enumeration<PooledConnection> enumerate = connections.elements();
		while (enumerate.hasMoreElements()) {
			pConn = (PooledConnection) enumerate.nextElement();
			// ���æ���� 5 ��
			if (pConn.isBusy()) {
				wait(5000); // �� 5 ��
			}
			// 5 ���ֱ�ӹر���
			closeConnection(pConn.getConnection());
			// �����ӳ�������ɾ����
			connections.removeElement(pConn);
		}
		// �����ӳ�Ϊ��
		connections = null;
	}
	/**
	 * 
	 * �ر�һ�����ݿ�����
	 *
	 * 
	 * 
	 * @param ��Ҫ�رյ����ݿ�����
	 * 
	 */
	private void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println(" �ر����ݿ����ӳ��� " + e.getMessage());
		}
	}
	/**
	 * 
	 * ʹ����ȴ������ĺ�����
	 *
	 * 
	 * 
	 * @param �����ĺ�����
	 * 
	 */
	private void wait(int mSeconds) {
		try {
			Thread.sleep(mSeconds);
		} catch (InterruptedException e) {
		}
	}

	/**
	 *
	 * 
	 * 
	 * �ڲ�ʹ�õ����ڱ������ӳ������Ӷ������
	 * 
	 * ��������������Ա��һ�������ݿ�����ӣ���һ����ָʾ�������Ƿ�
	 * 
	 * ����ʹ�õı�־��
	 * 
	 */

	class PooledConnection {
		Connection connection = null;// ���ݿ�����
		boolean busy = false; // �������Ƿ�����ʹ�õı�־��Ĭ��û������ʹ��
		// ���캯��������һ�� Connection ����һ�� PooledConnection ����
		public PooledConnection(Connection connection) {
			this.connection = connection;
		}
		// ���ش˶����е�����
		public Connection getConnection() {
			return connection;
		}

		// ���ô˶���ģ�����
		public void setConnection(Connection connection) {
			this.connection = connection;
		}
		// ��ö��������Ƿ�æ
		public boolean isBusy() {
			return busy;
		}
		// ���ö������������æ
		public void setBusy(boolean busy) {
			this.busy = busy;
		}
	}
}