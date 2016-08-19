package core.jdbc.db.mysql;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
//import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class DBQuery extends ConnectionPoolManager {
	private final Logger logger = Logger.getLogger("jdbc.db.mysql.DBQuery");

	private String table_name = "";
	private Object primary_key = "id";
	private String field = "*";
	private String where = "";
	private String order = "";
	private String group = "";
	private String limit = "";
	private String insertType = "INTO";
	private Class<?> entityClass = null;
	private Map<String, Object> whereMap = new HashMap<String, Object>();
	private Object[] whereParamters = null;
	private Map<String, Object> updateMap = new HashMap<String, Object>();
	private Object[] updateParamters = null;
	private Map<String, Object> insertMap = new HashMap<String, Object>();
	private Object[] insertParamters = null;
	private String read = "read";
	private final String write = "write";
	private Connection readConnection = null;
	private Connection writeConnection = null;
	private String jdbcDsn = "test";
	private boolean isTransaction = false;
	private boolean isTransactionSuccess = false;

	private PreparedStatement preparedStatement = null;
	private static Map<String, DBQuery> instances = new HashMap<String, DBQuery>();

	public DBQuery(String jdbcDsn) throws SQLException, IOException {
		super();
		this.jdbcDsn = jdbcDsn;
	}

	public static DBQuery instance(String jdbcDsn) throws SQLException, IOException, InterruptedException {
		DBQuery instanceDBQuery = null;
		if (instances.containsKey(jdbcDsn)) {
			instanceDBQuery = instances.get(jdbcDsn);
		} else {
			instanceDBQuery = new DBQuery(jdbcDsn);
		}
		emptyProperty(instanceDBQuery);
		instances.put(jdbcDsn, instanceDBQuery);
		return instanceDBQuery;
	}

	public static void emptyProperty(DBQuery instance) {
		instance.table_name = "";
		instance.primary_key = "id";
		instance.field = "*";
		instance.where = "";
		instance.order = "";
		instance.group = "";
		instance.limit = "";
		instance.entityClass = null;
		instance.whereMap = new HashMap<String, Object>();
		instance.whereParamters = null;
		instance.updateMap = new HashMap<String, Object>();
		instance.updateParamters = null;
		instance.insertMap = new HashMap<String, Object>();
		instance.insertParamters = null;
		instance.isTransaction = false;
		instance.isTransactionSuccess = false;
	}

	public DBQuery table(String table_name) {
		this.table_name = table_name;
		return this;
	}

	public DBQuery table(Class<?> clazz) {
		this.entityClass = clazz;
		this.table_name = this.getClassName(clazz);
		return this;
	}

	public DBQuery jointable(String table1_left_join_table2_ON_1_2) {
		return this.table(table1_left_join_table2_ON_1_2);
	}

	public DBQuery insertType(String type) {
		this.insertType = type;
		return this;
	}

	public DBQuery primaryKey(String primary_key) {
		this.setPrimary_key(primary_key);
		return this;
	}

	public void setPrimary_key(String primary_key) {
		this.primary_key = primary_key;
	}

	public Object getPrimary_key() {
		return this.primary_key;
	}

	public DBQuery where(String whereSQL) {
		if (this.where.startsWith(" WHERE")) {
			this.where = this.where + " AND " + whereSQL;
		} else {
			this.where = " WHERE " + whereSQL;
		}
		return this;
	}

	public DBQuery where(HashMap<String, String> whereCondition) {
		this.whereMap.putAll(whereCondition);
		return this;
	}

	public DBQuery group(String groupSQL) {
		if (this.group.startsWith(" GROUP BY")) {
			this.group = this.group + ", " + groupSQL;
		} else {
			this.group = " GROUP BY" + groupSQL;
		}
		return this;
	}

	public DBQuery order(String orderSQL) {
		if (this.order.startsWith(" ORDER BY")) {
			this.order = this.order + ", " + orderSQL;
		} else {
			this.order = " ORDER BY" + orderSQL;
		}
		return this;
	}

	public DBQuery limit(int offset, int rows) {
		this.limit = " LIMIT " + offset + ", " + rows + ";";
		return this;
	}

	private void where() {
		if (whereMap != null && whereMap.size() > 0) {
			this.whereParamters = new Object[whereMap.size()];
			StringBuilder whereSQL = new StringBuilder("");
			int i = 0;
			for (String key : whereMap.keySet()) {
				Object value = whereMap.get(key);
				if (whereSQL.toString().equals("")) {
					if (value.getClass().isArray()) {
						whereSQL.append(key + " IN( ? ) ");

					} else {
						whereSQL.append(key + " = ? ");
					}
				} else {
					if (value.getClass().isArray()) {
						whereSQL.append(" AND " + key + " IN( ? ) ");

					} else {
						whereSQL.append(" AND " + key + " = ? ");
					}
				}
				this.whereParamters[i] = value;
				i++;
			}
			if (this.where.startsWith(" WHERE")) {
				this.where = this.where + " AND " + whereSQL.toString();
			} else {
				this.where = " WHERE " + whereSQL.toString();
			}
		}
	}

	private String sql() {
		this.where();
		String sql = "SELECT " + this.field + " FROM " + this.table_name + this.where + this.order + this.group
				+ this.limit + ";";
		return sql;
	}

	public List<Map<String, Object>> getList() throws SQLException, InterruptedException {
		String sql = this.sql();
		if (this.whereParamters == null) {
			return this.getList(sql);
		}
		return this.getList(sql, whereParamters);
	}

	public List<Map<String, Object>> getList(String sql, ArrayList<Object> paramters)
			throws SQLException, InterruptedException {
		if (paramters != null && paramters.size() > 0) {
			Object[] paramter = paramters.toArray();
			return this.getList(sql, paramter);
		}
		return this.getList(sql);
	}

	private List<Map<String, Object>> getList(String sql, Object... paramters)
			throws SQLException, InterruptedException {
		ResultSet rs = null;
		try {
			long start = System.currentTimeMillis();
			if (this.readConnection == null)
				this.readConnection = this.getConnection(this.jdbcDsn + "." + read);
			preparedStatement = this.readConnection.prepareStatement(sql);//
			rs = this.executeForQuery(sql, paramters);
			List<Map<String, Object>> list = resultSetToListMap(rs);
			// logger.info("excuse sql:" + preparedStatement.toString());
			// logger.info("excuse sql:" + getQueryString(sql, paramters));
			System.out.println("使用Statment耗时：" + (System.currentTimeMillis() - start) + " ms");
			return list;
		} catch (SQLException e) {
			logger.severe("error sql:" + sql);
			e.printStackTrace();
			throw new SQLException(e);
		} finally {
			rs.close();
		}
	}

	/**
	 * 执行返回泛型集合的SQL语句
	 * 
	 * @param cls
	 *            泛型类型
	 * @param sql
	 *            查询SQL语句
	 * @return 泛型集合
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getEntityList() throws SQLException {
		if (this.entityClass == null)
			throw new SQLException("this.entityClass is null");
		return (List<T>) getEntityList(this.entityClass, this.sql());
	}

	private <T> List<T> getEntityList(Class<T> entityClassT, String sql) throws SQLException {
		List<T> list = new ArrayList<T>();
		ResultSet rs = null;
		try {
			if (this.readConnection == null)
				this.readConnection = this.getConnection(this.jdbcDsn + "." + read);
			preparedStatement = this.readConnection.prepareStatement(sql);
			rs = this.executeForQuery(sql, whereParamters);
			// rs = ps.executeQuery();
			while (rs.next()) {
				T obj = (T) executeResultSet(entityClassT, rs);
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe("error sql:" + sql);
			throw new SQLException(e);
		} finally {
			rs.close();
		}
		return list;
	}

	public Object getOne() throws SQLException, InterruptedException {
		return this.getOne(this.sql(), whereParamters);
	}

	@SuppressWarnings("unused")
	private Object getOne(String sql, ArrayList<Object> paramters) throws SQLException, InterruptedException {
		if (paramters != null && paramters.size() > 0) {
			Object[] paramter = paramters.toArray();
			return this.getOne(sql, paramter);
		}
		return this.getOne(sql);
	}

	private Object getOne(String sql, Object... paramters) throws SQLException, InterruptedException {
		Object result = null;
		ResultSet rs = null;
		try {
			if (this.readConnection == null)
				this.readConnection = this.getConnection(this.jdbcDsn + "." + read);
			preparedStatement = this.readConnection.prepareStatement(sql);
			rs = this.executeForQuery(sql, paramters);
			if (rs.next()) {
				result = rs.getObject(1);
			}
			return result;
		} catch (SQLException e) {
			logger.severe("error sql:" + sql);
			throw new SQLException(e);
		} finally {
			rs.close();
		}
	}

	public DBQuery setUpdateData(HashMap<String, String> updateData) {
		updateMap.putAll(updateData);
		return this;
	}

	private String updateSQL() throws SQLException {
		this.where();
		StringBuilder updateSQL = new StringBuilder("");
		if (updateMap != null && updateMap.size() > 0) {
			updateParamters = new Object[updateMap.size()];
			int i = 0;
			for (String key : updateMap.keySet()) {
				if (updateSQL.toString().equals("")) {
					updateSQL.append(key + " = ? ");
				} else {
					updateSQL.append(", " + key + " = ? ");
				}
				updateParamters[i] = updateMap.get(key);
				i++;
			}
		}
		if (updateSQL.equals(""))
			throw new SQLException("Update SQL paramters error");
		String sql = "UPDATE " + this.table_name + " SET " + updateSQL.toString() + this.where + ";";
		return sql;
	}

	public void setBatchUpdateSql() throws SQLException, InterruptedException {
		if (this.writeConnection == null)
			this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
		if (this.isTransaction)
			this.writeConnection.setAutoCommit(false);
		String sql = this.updateSQL();
		preparedStatement = this.writeConnection.prepareStatement(sql);
		// 设值SQL
		this.resolveUpdateSql(sql, updateParamters);
		preparedStatement.addBatch();
	}

	public int[] executeBatchUpdate() throws SQLException {
		try {
			int[] result = preparedStatement.executeBatch();
			preparedStatement.clearBatch();
			if (this.isTransaction) {
				this.writeConnection.commit();
				this.writeConnection.setAutoCommit(true);
				this.isTransactionSuccess = true;
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			if (this.isTransaction) {
				this.writeConnection.rollback();
			}
		} finally {

		}
		return null;
	}

	public int update() throws SQLException, InterruptedException {
		return this.update(this.updateSQL(), updateParamters);
	}

	private int update(String sql, ArrayList<Object> paramters) throws SQLException, InterruptedException {
		if (paramters != null && paramters.size() > 0) {
			Object[] paramter = paramters.toArray();
			return this.update(sql, paramter);
		}
		return this.update(sql);
	}

	private int update(String sql, Object... paramters) throws SQLException, InterruptedException {
		try {
			if (this.writeConnection == null)
				this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
			if (this.isTransaction)
				this.writeConnection.setAutoCommit(false);
			preparedStatement = this.writeConnection.prepareStatement(sql);
			// 设值SQL
			this.resolveUpdateSql(sql, paramters);
			// 执行SQL
			int result = preparedStatement.executeUpdate();
			if (this.isTransaction) {
				this.writeConnection.commit();
				this.writeConnection.setAutoCommit(true);
				this.isTransactionSuccess = true;
			}
			return result;
		} catch (SQLException e) {
			logger.severe("error sql:" + sql);
			if (this.isTransaction) {
				this.writeConnection.rollback();
			}
			throw new SQLException(e);
		} finally {
		}
	}

	/**
	 * 批量更新数据
	 * 
	 * @param sqlList
	 *            一组sql
	 * @return
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public int[] batchUpdate(List<String> sqlList) throws SQLException, InterruptedException {
		int[] result = new int[] {};
		Statement statenent = null;
		if (this.writeConnection == null)
			this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
		try {
			if (this.isTransaction)
				this.writeConnection.setAutoCommit(false);
			statenent = this.writeConnection.createStatement();
			for (String sql : sqlList) {
				statenent.addBatch(sql);
			}
			result = statenent.executeBatch();
			this.writeConnection.commit();
		} catch (SQLException e) {
			try {
				this.writeConnection.rollback();
			} catch (SQLException e1) {
				throw new ExceptionInInitializerError(e1);
			}
			throw new ExceptionInInitializerError(e);
		} finally {
			statenent.close();
		}
		return result;
	}

	public DBQuery setInsertData(HashMap<String, String> insertData) {
		insertMap.putAll(insertData);
		return this;
	}

	private String insertSQL() throws SQLException {
		this.where();
		StringBuilder insertSQL = new StringBuilder("");
		StringBuilder insertSQLValue = new StringBuilder("");
		if (insertMap != null && insertMap.size() > 0) {
			insertParamters = new Object[insertMap.size()];
			int i = 0;
			for (String key : insertMap.keySet()) {
				if (insertSQL.toString().equals("")) {
					insertSQL.append(key);
					insertSQLValue.append("?");
				} else {
					insertSQL.append(", " + key);
					insertSQLValue.append(", ?");
				}
				insertParamters[i] = insertMap.get(key);
				i++;
			}
		}
		if (insertSQL.equals(""))
			throw new SQLException("insert SQL paramters error");
		String sql = "INSERT " + this.insertType + " " + this.table_name + " ( " + insertSQL.toString() + ") VALUES ("
				+ insertSQLValue.toString() + ")";
		return sql;
	}

	//设置批量插入
	public void setBatchInsertSql() throws SQLException, InterruptedException {
		if (this.writeConnection == null)
			this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
		if (this.isTransaction)
			this.writeConnection.setAutoCommit(false);
		String sql = this.insertSQL();
		preparedStatement = this.writeConnection.prepareStatement(sql);
		// 设值SQL
		this.resolveUpdateSql(sql, insertParamters);
		preparedStatement.addBatch();
	}

	//执行批量插入
	public int[] executeBatchInsert() throws SQLException {
		try {
			int[] result = preparedStatement.executeBatch();
			preparedStatement.clearBatch();
			if (this.isTransaction) {
				this.writeConnection.commit();
				this.writeConnection.setAutoCommit(true);
				this.isTransactionSuccess = true;
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			if (this.isTransaction) {
				this.writeConnection.rollback();
			}
		} finally {

		}
		return null;
	}
	
	public Object insert() throws SQLException, InterruptedException {
		return this.insert(this.insertSQL(), insertParamters);
	}

	public Object insert(String sql, ArrayList<Object> paramters) throws Exception {
		if (paramters != null && paramters.size() > 0) {
			Object[] paramter = paramters.toArray();
			return this.insert(sql, paramter);
		}
		return this.insert(sql);
	}

	private Object insert(String sql, Object... paramters) throws SQLException, InterruptedException {
		ResultSet rs = null;
		Object result = null;
		try {
			if (this.writeConnection == null)
				this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
			if (this.isTransaction)
				this.writeConnection.setAutoCommit(false);
			preparedStatement = this.writeConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			if (paramters != null && paramters.length > 0) {
				for (int i = 0; i < paramters.length; i++) {
					preparedStatement.setObject(i + 1, paramters[i]);
				}
			}
			preparedStatement.execute();
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getObject(1);
			}
			if (this.isTransaction) {
				this.writeConnection.commit();
				this.writeConnection.setAutoCommit(true);
				this.isTransactionSuccess = true;
			}
			logger.info("excuse sql:" + getQueryString(sql, paramters));
			return result;
		} catch (SQLException e) {
			logger.severe("error sql:" + sql);
			if (this.isTransaction)
				this.writeConnection.rollback();
			throw new SQLException(e);
		}

	}

	// 保存对象 （未关闭数据库 资源） 表名与类名相同
	public Object insert(Object object) throws Exception {
		ResultSet rs = null;
		Object result = null;
		try {
			Class<?> objectClass = object.getClass();
			String tableName = getClassName(objectClass);
			Field[] fields = objectClass.getDeclaredFields();
			List<Object> valueObj = new ArrayList<Object>();
			StringBuffer sql = new StringBuffer("INSERT " + this.insertType + " " + tableName + " (");
			StringBuffer sqlParamters = new StringBuffer("");
			for (int i = 0; i < fields.length; i++) {
				String fieldName = fields[i].getName();
				String methodNameBegin = fieldName.substring(0, 1).toUpperCase();
				String methodName = "get" + methodNameBegin + fieldName.substring(1);

				Method method = objectClass.getMethod(methodName);
				Object tempObj = method.invoke(object);
				if (tempObj != null) {
					valueObj.add(tempObj);
					sql.append(fieldName).append(",");
					sqlParamters.append("?,");
				}
			}
			if (valueObj.size() == 0) {
				return 0;
			} else {
				// 最后一位为,，去除
				if (sql.charAt(sql.length() - 1) == ',') {
					sql.deleteCharAt(sql.length() - 1);
				}
				if (sqlParamters.charAt(sqlParamters.length() - 1) == ',') {
					sqlParamters.deleteCharAt(sqlParamters.length() - 1);
				}
				sql.append(") VALUES (").append(sqlParamters).append(")");
				if (this.writeConnection == null)
					this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
				if (this.isTransaction)
					this.writeConnection.setAutoCommit(false);
				preparedStatement = this.writeConnection.prepareStatement(sql.toString(),
						PreparedStatement.RETURN_GENERATED_KEYS);
				System.out.println(sql.toString());
				for (int i = 1; i <= valueObj.size(); i++) {
					preparedStatement.setObject(i, valueObj.get(i - 1));
				}
				// preparedStatement.executeUpdate();
				preparedStatement.execute();
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					result = rs.getObject(1);
				}
				if (this.isTransaction) {
					this.writeConnection.commit();
					this.writeConnection.setAutoCommit(true);
					this.isTransactionSuccess = true;
				}
			}
			return result;
		} catch (SQLException e) {
			if (this.isTransaction)
				this.writeConnection.rollback();
			throw new SQLException(e);
		}
	}

	private String deleteSQL() throws SQLException {
		this.where();
		String sql = "DELETE" + this.table_name + this.where + ";";
		return sql;
	}

	public int delete() throws SQLException, InterruptedException {
		return this.delete(this.deleteSQL());
	}

	public int delete(String sql) throws SQLException, InterruptedException {
		try {
			if (this.writeConnection == null)
				this.writeConnection = this.getConnection(this.jdbcDsn + "." + this.write);
			if (this.isTransaction)
				this.writeConnection.setAutoCommit(false);
			preparedStatement = this.writeConnection.prepareStatement(sql);
			int resultInt = preparedStatement.executeUpdate();
			if (this.isTransaction) {
				this.writeConnection.commit();
				this.writeConnection.setAutoCommit(true);
				this.isTransactionSuccess = true;
			}
			logger.info("excuse sql:" + getQueryString(sql));
			return resultInt;
		} catch (SQLException e) {
			if (this.isTransaction)
				this.writeConnection.rollback();
			logger.severe("error sql:" + sql);
			throw new SQLException(e);
		}

	}

	/**
	 * 回滚事务并关闭数据库连接
	 * 
	 * @throws SQLException
	 */
	public void rollback() {
		try {
			this.writeConnection.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// =======================================================//
	// 得到类名，不包含包名
	private String getClassName(Class<?> className) {
		String temp = className.getName();
		return temp.substring(temp.lastIndexOf(".") + 1);
	}

	// 分解UPDATE SQL 设置值
	private void resolveUpdateSql(String sql, Object... paramters) throws SQLException {
		if (paramters != null && paramters.length > 0) {
			int i = 0;
			for (; i < paramters.length; i++) {
				preparedStatement.setObject(i + 1, paramters[i]);
			}
			if (whereParamters != null && whereParamters.length > 0) {
				for (int j = 0; j < whereParamters.length; j++) {
					preparedStatement.setObject(i + 1, whereParamters[j]);
					i++;
				}
			}
		}
	}

	// 根据ResultSet 取得列表值
	private ResultSet executeForQuery(String sql, Object... paramters) throws SQLException {
		if (paramters != null && paramters.length > 0) {
			for (int i = 0; i < paramters.length; i++) {
				preparedStatement.setObject(i + 1, paramters[i]);
			}
		}
		return preparedStatement.executeQuery();
	}

	// 根据ResultSet 取得列表值
	private static List<Map<String, Object>> resultSetToListMap(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = null;
		while (rs.next()) {
			if (md == null)
				md = rs.getMetaData();
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i < md.getColumnCount(); i++) {
				map.put(md.getColumnLabel(i), rs.getObject(i));
			}
			list.add(map);
		}
		return list;
	}

	/**
	 * 将一条记录转成一个对象
	 * 
	 * @param cls
	 *            泛型类型
	 * @param rs
	 *            ResultSet对象
	 * @return 泛型类型对象
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws SQLException
	 */
	private static <T> T executeResultSet(Class<T> cls, ResultSet rs)
			throws InstantiationException, IllegalAccessException, SQLException {
		T obj = cls.newInstance();
		ResultSetMetaData rsm = rs.getMetaData();
		int columnCount = rsm.getColumnCount();
		// Field[] fields = cls.getFields();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			for (int j = 1; j <= columnCount; j++) {
				String columnName = rsm.getColumnName(j);
				if (fieldName.equalsIgnoreCase(columnName)) {
					Object value = rs.getObject(j);
					field.setAccessible(true);
					field.set(obj, value);
					break;
				}
			}
		}
		return obj;
	}

	private String getQueryString(String sqlTemplate, Object... parameterValues) {
		int len = sqlTemplate.length();
		StringBuffer t = new StringBuffer(len * 2);
		if (parameterValues != null) {
			int i = 0, limit = 0, base = 0;
			while ((limit = sqlTemplate.indexOf('?', limit)) != -1) {
				t.append(sqlTemplate.substring(base, limit));
				t.append("'" + parameterValues[i] + "'");
				i++;
				limit++;
				base = limit;
			}
			if (base < len) {
				t.append(sqlTemplate.substring(base));
			}
		}
		return t.toString();
	}

	public boolean isTransaction() {
		return isTransaction;
	}

	public void setTransaction(boolean isTransaction) {
		this.isTransaction = isTransaction;
	}

	public boolean isTransactionSuccess() {
		return isTransactionSuccess;
	}

	public void setTransactionSuccess(boolean isTransactionSuccess) {
		this.isTransactionSuccess = isTransactionSuccess;
	}

}
