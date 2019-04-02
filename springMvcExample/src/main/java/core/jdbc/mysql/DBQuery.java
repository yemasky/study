package core.jdbc.mysql;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import core.util.DBCache;
import core.util.Encrypt;

public abstract class DBQuery {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private String table_name = "";
	private Object primary_key = "id";
	private String field = "*";
	private String insertType = "INTO";
	private Class<?> entityClass = null;
	private String read = "read";
	private final String write = "write";
	private String jdbcDsn = "test";
	private boolean isTransaction = false;
	private boolean isTransactionSuccess = false;
	private int cacheTime = 0;
	protected String client_key;
	// 当前使用connection
	private Connection writeConnection = null;
	private Connection readConnection = null;
	
	public DBQuery(String jdbcDsn) throws SQLException {
		this.jdbcDsn = jdbcDsn;
		ConnectionPoolManager.instance();
	}
	
	protected DBQuery setDsn(String jdbcDsn) {
		this.jdbcDsn = jdbcDsn;
		return this;
	}

	public DBQuery emptyProperty() {
		this.primary_key = "id";
		this.field = "*";
		this.entityClass = null;
		this.cacheTime = 0;
		return this;
	}

	public DBQuery Cache(int time) {
		this.cacheTime = time;
		return this;
	}

	public Connection getWriteConnection() {
		return this.writeConnection;
	}
	
	public Connection getReadConnection() {
		return this.readConnection;
	}
	
	public Connection setWriteConnection(Connection writeConnection) {
		return this.writeConnection = writeConnection;
	}
	
	public Connection setReadConnection(Connection readConnection) {
		return this.readConnection = readConnection;
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
	
	public DBQuery setField(String field) {
		this.field = field;
		return this;
	}

	private String sql() throws SQLException {
		return "SELECT " + this.field + " FROM " + this.table_name;
	}

	public List<Map<String, Object>> getList() throws SQLException {
		String sql = this.sql();
		return this.getList(sql);
	}
	
	public List<Map<String, Object>> getList(WhereRelation whereRelation) throws SQLException {
		String sql = this.sql() + whereRelation.getWhere();
		return this.getList(sql, whereRelation.getWhereParamters());
	}

	private List<Map<String, Object>> getList(String sql, Object... paramters) throws SQLException  {
		if (this.cacheTime > 0) {
			String key = Encrypt.md5Lower(sql + paramters.toString() + this.jdbcDsn);
			List<Map<String, Object>> listCache = DBCache.instance().get(key);
			if (listCache != null && listCache.size() > 0) {
				return listCache;
			}
		}
		ResultSet rs = null;
		try {
			long start = System.currentTimeMillis();
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);//
			rs = this.executeForQuery(preparedStatement, paramters);
			List<Map<String, Object>> list = resultSetToListMap(rs);
			logger.info("查询耗时：" + (System.currentTimeMillis() - start) + " ms");
			if (this.cacheTime > 0) {
				String key = Encrypt.md5Lower(sql + paramters.toString() + this.jdbcDsn);
				DBCache.instance().set(key, (Serializable) list, this.cacheTime);
			}
			return list;
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql);
		} finally {
			rs.close();
		}
		return null;
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
	public <T> List<T> getEntityList(WhereRelation whereRelation) throws SQLException {
		if (this.entityClass == null)
			throw new SQLException("this.entityClass is null");
		return (List<T>) getEntityList(this.entityClass, whereRelation);
	}

	public <T> T getEntity(WhereRelation whereRelation) throws SQLException {
		List<T> entityList = this.getEntityList(whereRelation);
		if(entityList == null) return null;
		if (entityList.size() > 0)
			return entityList.get(0);
		return null;
	}

	private <T> List<T> getEntityList(Class<T> entityClassT, WhereRelation whereRelation) throws SQLException {
		String sql = this.sql() + whereRelation.getWhere();
		List<T> list = null;
		ResultSet rs = null;
		try {
			list = new ArrayList<T>();
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);
			logger.debug(sql);
			rs = this.executeForQuery(preparedStatement, whereRelation.getWhereParamters());
			ResultSetMetaData rsm = rs.getMetaData();
			int columnCount = rsm.getColumnCount();
			Field[] fields = entityClassT.getDeclaredFields();
			while (rs.next()) {
				T objClass = entityClassT.newInstance();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					String fieldName = field.getName();
					for (int j = 1; j <= columnCount; j++) {
						String columnName = rsm.getColumnName(j);
						if (fieldName.equalsIgnoreCase(columnName)) {
							Object value = rs.getObject(j);
							field.setAccessible(true);
							field.set(objClass, value);
							break;
						}
					}
				}
				list.add(objClass);
			}
			if(list.size() > 0) return (List<T>) list;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
		} finally {
			if(rs != null) rs.close();
		}
		return null;
	}

	public Object getOne(WhereRelation whereRelation) throws SQLException, InterruptedException {
		String sql = this.sql() + whereRelation.getWhere();
		Object result = null;
		ResultSet rs = null;
		try {
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);
			rs = this.executeForQuery(preparedStatement, whereRelation.getWhereParamters());
			if (rs.next()) {
				result = rs.getObject(1);
			}
			return result;
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql);
		} finally {
			if(rs != null) rs.close();
		}
		return null;
	}

	/*
	 *  数据更新 updateData<field,vaule>
	 */
	public int update(HashMap<String, Object> updateData, WhereRelation whereRelation) throws SQLException {
		StringBuilder updateSQL = new StringBuilder("");
		List<Object> updateParamtersList = new ArrayList<>();
		if (updateData != null && !updateData.isEmpty() && updateData.size() > 0) {
			for(Entry<String, Object> updateEntry : updateData.entrySet()) {
				updateSQL.append(", " + updateEntry.getKey() + " = ? ");
				updateParamtersList.add(updateEntry.getValue());
			}
		}
		String sql = "UPDATE " + this.table_name + " SET " + updateSQL.toString().substring(2) + whereRelation.getWhere();
		try {
			PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql);
			logger.info("update sql:" + sql);
			Object[] paramters = null;
			int i = 0;
			if(!updateParamtersList.isEmpty() && updateParamtersList.size() > 0) {
				paramters = updateParamtersList.toArray();
				for (; i < paramters.length; i++) {
					preparedStatement.setObject(i + 1, paramters[i]);
				}
				logger.info("update data:" + Arrays.toString(paramters));
			}
			
			paramters = whereRelation.getWhereParamters();
			if(paramters != null && paramters.length > 0) {
				//int j = i > 0 ? i - 1 : i;
				for (int j = 0; j < paramters.length; j++) {
					preparedStatement.setObject(j + i + 1, paramters[j]);
				}
				logger.info("update whereCondition:" + Arrays.toString(paramters));
			}
			//this.resolveUpdateSql(preparedStatement, updateParamtersList.toArray(), whereRelation.getWhereParamters());
			// 执行SQL
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
		} finally {
		}
		return 0;
	}
	/*
	 *  删除数据
	 */
	public int delete(WhereRelation whereRelation) throws SQLException, InterruptedException {
		String sql = "DELETE FROM " + this.table_name + whereRelation.getWhere();
		try {
			PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql);
			Object[] paramters = whereRelation.getWhereParamters();
			if (paramters != null && paramters.length > 0) {
				for (int i = 0; i < paramters.length; i++) {
					preparedStatement.setObject(i + 1, paramters[i]);
				}
			}
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
		}
		return 0;
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
		Statement statement = null;
		try {
			statement = this.thisWriteConnection().createStatement();
			for (String sql : sqlList) {
				statement.addBatch(sql);
			}
			result = statement.executeBatch();
		} catch (SQLException e) {
			
		} finally {
			statement.close();
		}
		return result;
	}

	public void insert(HashMap<String, Object> insertData) throws SQLException {
		this.insertIdEntity(insertData);
	}

	// 保存对象 （未关闭数据库 资源） 表名与类名相同
	public void insertObject(Object object) throws Exception {
		this.insertEntity(object);
	}

	/*
	 *     保存对象 （未关闭数据库 资源） 表名与类名相同
	 *      返回递增的ID
	 */ 
	public Object insertIdEntity(Object object) throws Exception {
		return this.insertEntity(object);
	}
	// 返回递增的ID
	public Object insertIdEntity(HashMap<String, Object> insertData) throws SQLException {
		StringBuilder insertSQL = new StringBuilder("");
		StringBuilder insertSQLValue = new StringBuilder("");
		Object[] insertParamters = new Object[insertData.size()];
		if (insertData != null && insertData.size() > 0) {
			int i = 0;
			for(Entry<String, Object> insertEntry : insertData.entrySet()) {
				insertSQL.append(", " + insertEntry.getKey());
				insertSQLValue.append(", ?");
				insertParamters[i] = insertEntry.getValue();
				i++;
			}
		}
		if (insertSQL.toString().equals(""))
			throw new SQLException("insert SQL paramters error");
		String sql = "INSERT " + this.insertType + " " + this.table_name + " ( " + insertSQL.toString().substring(2) + ") VALUES ("
				+ insertSQLValue.toString().substring(2) + ")";
		return this.insert(sql, insertParamters);
	}

	private Object insert(String sql, Object... paramters) throws SQLException {
		ResultSet rs = null;
		Object result = null;
		try {
			PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			if (paramters != null && paramters.length > 0) {
				for (int i = 0; i < paramters.length; i++) {
					preparedStatement.setObject(i + 1, paramters[i]);
				}
			}
			logger.debug(sql);
			logger.info("insert paramters:" + Arrays.toString(paramters));
			preparedStatement.execute();
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getObject(1);
			}
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql", e);
		}
		return result;
	}

	private Object insertEntity(Object object) throws Exception {
		ResultSet rs = null;
		Object result = null;
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
			PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql.toString(),
					PreparedStatement.RETURN_GENERATED_KEYS);
			for (int i = 1; i <= valueObj.size(); i++) {
				preparedStatement.setObject(i, valueObj.get(i - 1));
			}
			logger.debug(sql.toString());
			// preparedStatement.executeUpdate();
			preparedStatement.execute();
			rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				result = rs.getObject(1);
			}
		}
		return result;
	}

	// 得到类名，不包含包名
	private String getClassName(Class<?> className) {
		String temp = className.getName();
		return temp.substring(temp.lastIndexOf(".") + 1).toLowerCase();
	}

	// 根据ResultSet 取得列表值
	private ResultSet executeForQuery(PreparedStatement preparedStatement, Object... paramters) throws SQLException {
		if (paramters != null && paramters.length > 0) {
			for (int i = 0; i < paramters.length; i++) {
				preparedStatement.setObject(i + 1, paramters[i]);
			}
			MDC.put("APP_NAME", "mysql_info");
			logger.info("select whereCondition:" + Arrays.toString(paramters));
		}
		return preparedStatement.executeQuery();
	}

	// 根据ResultSet 取得列表值
	private static List<Map<String, Object>> resultSetToListMap(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		while (rs.next()) {
			//if (md == null) md = rs.getMetaData();
			Map<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
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
	@SuppressWarnings("unused")
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

	private Connection thisWriteConnection() throws SQLException {
		String key = this.jdbcDsn + "." + this.write;
		if(this.writeConnection == null || !this.writeConnection.isValid(1)) {
			this.writeConnection = ConnectionPoolManager.instance().getConnection(key);
		}
		return writeConnection;
	}
	
	private Connection thisReadConnection() throws SQLException {
		String key = this.jdbcDsn + "." + this.read;
		if(this.readConnection == null || !this.readConnection.isValid(1)) {
			this.readConnection = ConnectionPoolManager.instance().getConnection(key);
		}
		return readConnection;
	}
	
	//释放连接
	public void freeConnection() throws SQLException {
		if(this.readConnection != null && this.readConnection.isValid(1)) 
			ConnectionPoolManager.instance().freeConnection(this.jdbcDsn + "." + this.read, this.readConnection);
		if(this.writeConnection != null && this.writeConnection.isValid(1)) 
			ConnectionPoolManager.instance().freeConnection(this.jdbcDsn + "." + this.write, this.writeConnection);
	}
	
	public void freeConnection(String jdbcDsn, Connection connection) throws SQLException {
		if(connection.isValid(1)) 
			ConnectionPoolManager.instance().freeConnection(jdbcDsn, connection);
	}
	
	protected boolean isTransaction() {
		return isTransaction;
	}

	protected void setTransaction(boolean isTransaction) throws SQLException {
		this.isTransaction = isTransaction;
		if(isTransaction) this.writeConnection.setAutoCommit(false);//开始事务
	}
	
	public void commit() throws SQLException {
		this.writeConnection.commit();
	}
	
	/**
	 * 回滚事务并关闭数据库连接
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		this.writeConnection.rollback();
	}
			
	protected boolean isTransactionSuccess() {
		return isTransactionSuccess;
	}

	protected void setTransactionSuccess(boolean isTransactionSuccess) {
		this.isTransactionSuccess = isTransactionSuccess;
	}

}

