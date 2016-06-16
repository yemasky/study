package jdbc.mysql;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Element;

//import com.chunkyo.db.ParseDSConfig;
//import com.chunkyo.db.DSConfigBean;
/**
 * @author chenyanlin
 *
 */
public class ConnectionManager {
	static private ConnectionManager instance;// Ψһ���ݿ����ӳع���ʵ����
	static private int clients; // �ͻ�������
	private Vector<Config> drivers = new Vector<Config>();// ������Ϣ
	private Hashtable<String, ConnectionPools> pools = new Hashtable<String, ConnectionPools>();// ���ӳ�

	/**
	 * ʵ����������
	 */
	public ConnectionManager() {
		this.init();
	}

	/**
	 * �õ�Ψһʵ��������
	 * 
	 * @return
	 */
	static synchronized public ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		return instance;

	}

	/**
	 * �ͷ�����
	 * 
	 * @param connectionName
	 * @param connection
	 *            ConnectionPoolDataSource
	 */
	public void releaseConnection(String connectionName, Connection connection) {
		ConnectionPools pool = (ConnectionPools) pools.get(connectionName);// ���ݹؼ����ֵõ����ӳ�
		if (pool != null)
			pool.releaseConnection(connection);// �ͷ�����
	}

	/**
	 * �õ�һ�����Ӹ������ӳص�����name
	 * 
	 * @param name
	 * @return
	 */
	public Connection getConnection(String connectionName) {
		ConnectionPools pool = null;
		Connection connection = null;
		pool = (ConnectionPools) pools.get(connectionName);// �������л�ȡ���ӳ�
		connection = pool.getConnection();// ��ѡ�������ӳ��л������
		if (connection != null)
			System.out.println("�õ�����...");
		return connection;
	}

	/**
	 * �õ�һ�����ӣ��������ӳص����ֺ͵ȴ�ʱ��
	 * 
	 * @param name
	 * @param time
	 * @return
	 */
	public Connection getConnection(String connectionName, long timeout) {
		ConnectionPools pool = null;
		Connection connection = null;
		pool = (ConnectionPools) pools.get(connectionName);// �������л�ȡ���ӳ�
		connection = pool.getConnection(timeout);// ��ѡ�������ӳ��л������
		System.out.println("�õ�����...");
		return connection;
	}

	/**
	 * �ͷ���������
	 */
	public synchronized void release() {
		Enumeration<ConnectionPools> allpools = pools.elements();
		while (allpools.hasMoreElements()) {
			ConnectionPools pool = (ConnectionPools) allpools.nextElement();
			if (pool != null)
				pool.release();
		}
		pools.clear();
	}

	/**
	 * �������ӳ�
	 * 
	 * @param props
	 */
	private void createPools(Config config) {
		ConnectionPools pool = new ConnectionPools();
		pool.setConnectionName(config.getConnectionName());
		pool.setDriver(config.getDriver());
		pool.setUrl(config.getUrl());
		pool.setUser(config.getUsername());
		pool.setPassword(config.getPassword());
		pool.setMaxConnection(config.getMaxConnection());
		pools.put(config.getConnectionName(), pool);
		System.out.println("pool:" + config.getMaxConnection());
	}

	/**
	 * ��ʼ�����ӳصĲ���
	 */
	private void init() {
		// ������������
		this.loadDrivers();
		// �������ӳ�
		Iterator<Config> driver = drivers.iterator();
		while (driver.hasNext()) {
			this.createPools((Config) driver.next());
			System.out.println("�������ӳء�����");

		}
		System.out.println("�������ӳ���ϡ�����");
	}

	/**
	 * ������������
	 * 
	 * @param props
	 * @return 
	 */
	private Vector<Config> loadDrivers() {
		drivers = null;
		try {
	
			List<?> pools = null;
			Element pool = null;
			Iterator<?> allPool = pools.iterator();
			while (allPool.hasNext()) {
				pool = (Element) allPool.next();
				Config dscBean = new Config();
				dscBean.setType(pool.getAttribute("type"));
				dscBean.setConnectionName(pool.getAttribute("name"));
				System.out.println(dscBean.getConnectionName());
				dscBean.setDriver(pool.getAttribute("driver"));
				dscBean.setUrl(pool.getAttribute("url"));
				dscBean.setUsername(pool.getAttribute("username"));
				dscBean.setPassword(pool.getAttribute("password"));
				dscBean.setMaxConnection(Integer.parseInt(pool.getAttribute("maxconn")));
				drivers.add(dscBean);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		return drivers;
		// ��ȡ���ݿ������ļ�
		//drivers = config.readConfigInfo();
		//System.out.println("�����������򡣡���");
	}
}