package example.jdbc.mysql;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
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

import jdbc.db.mysql.ConnectionPoolManager;



public class DBQuery2 extends ConnectionPoolManager {

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
	//LoggableStatement
	private PreparedStatement preparedStatement = null;

	public DBQuery2(String jdbcDsn) throws SQLException, IOException {
		super();
		this.jdbcDsn = jdbcDsn;
	}

	public static DBQuery2 instance(String jdbcDsn) throws SQLException, IOException, InterruptedException {
		return new DBQuery2(jdbcDsn);
	}
	public DBQuery2 table(String table_name) {
		this.table_name = table_name;
		return this;
	}

	public DBQuery2 table(Class<?> clazz) {
		this.entityClass = clazz;
		this.table_name = this.getClassName(clazz);
		return this;
	}

	public DBQuery2 insertType(String type) {
		this.insertType = type;
		return this;
	}

	public DBQuery2 primaryKey(String primary_key) {
		this.setPrimary_key(primary_key);
		return this;
	}

	public void setPrimary_key(String primary_key) {
		this.primary_key = primary_key;
	}
	
	public Object getPrimary_key() {
		return this.primary_key;
	}
	
	public DBQuery2 where(String whereSQL) {
		if (this.where.startsWith(" WHERE")) {
			this.where = this.where + " AND " + whereSQL;
		} else {
			this.where = " WHERE " + whereSQL;
		}
		return this;
	}

	public DBQuery2 group(String groupSQL) {
		if (this.group.startsWith(" GROUP BY")) {
			this.group = this.group + ", " + groupSQL;
		} else {
			this.group = " GROUP BY" + groupSQL;
		}
		return this;
	}

	public DBQuery2 order(String orderSQL) {
		if (this.order.startsWith(" ORDER BY")) {
			this.order = this.order + ", " + orderSQL;
		} else {
			this.order = " ORDER BY" + orderSQL;
		}
		return this;
	}

	public DBQuery2 limit(int offset, int rows) {
		this.limit = " LIMIT " + offset + ", " + rows + ";";
		return this;
	}

	public DBQuery2 where(HashMap<String, String> whereData) {
		this.whereMap.putAll(whereData);
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
					if(value.getClass().isArray()) {
						whereSQL.append(key + " IN( ? ) ");

					} else {
						whereSQL.append(key + " = ? ");
					}
				} else {
					if(value.getClass().isArray()) {
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

	public DBQuery2 setInsertData(HashMap<String, String> insertData) {
		insertMap.putAll(insertData);
		return this;
	}

	private String sql() {
		this.where();
		String sql = "SELECT " + this.field + " FROM " + this.table_name + this.where + this.order + this.group
				+ this.limit + ";";
		return sql;
	}

	public List<Map<String, Object>> getList() throws SQLException, InterruptedException {
		String sql = this.sql();
		if(this.whereParamters == null) {
			return this.getList(sql);
		}
		return this.getList(sql, whereParamters);
	}

	public List<Map<String, Object>> getList(String sql) throws SQLException, InterruptedException {
		Object paramter = null;
		return this.getList(sql, paramter);
	}
	
	public List<Map<String, Object>> getList(String sql, ArrayList<Object> paramters) throws SQLException, InterruptedException {
		if(paramters != null && paramters.size() > 0) {
			Object[] paramter = paramters.toArray();
			return this.getList(sql, paramter);
		}
		return this.getList(sql);
	}

	public List<Map<String, Object>> getList(String sql, Object... paramters) throws SQLException, InterruptedException {
		ResultSet rs = null;
		try {
			long start = System.currentTimeMillis();  
			if(this.readConnection == null) this.readConnection = this.getConnection(this.jdbcDsn + "." + read);
			rs = this.execute(sql, this.readConnection, paramters);
			List<Map<String, Object>> list = resultSetToListMap(rs);
			//logger.info("excuse sql:" + preparedStatement.toString());
			//logger.info("excuse sql:" + getQueryString(sql, paramters));
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

	private ResultSet execute(String sql, Connection connection,  Object... paramters) throws SQLException {
		preparedStatement = connection.prepareStatement(sql);//.prepareStatement(sql);
		if (paramters != null && paramters.length > 0) {
			for (int i = 0; i < paramters.length; i++) {
				preparedStatement.setObject(i + 1, paramters[i]);
			}
		}
		return preparedStatement.executeQuery();
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
        return (List<T>) getEntityList(this.entityClass, this.sql());
    }
    
    public <T> List<T> getEntityList(Class<T> entityClassT, String sql) throws SQLException {
        List<T> list = new ArrayList<T>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
        	if(this.readConnection == null) this.readConnection = this.getConnection(this.jdbcDsn + "." + read);
            ps = (PreparedStatement) this.readConnection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
				T obj = (T) executeResultSet(entityClassT, rs);
                list.add(obj);
            }
        } catch (Exception e) {
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

	public Object getOne(String sql) throws SQLException, InterruptedException {
		Object paramter = null;
		return this.getOne(sql, paramter);
	}
	
	public Object getOne(String sql, ArrayList<Object> paramters) throws SQLException, InterruptedException {
		if(paramters != null && paramters.size() > 0) {
			Object[] paramter = paramters.toArray();
			return this.getOne(sql, paramter);
		}
		return this.getOne(sql);
	}

	public Object getOne(String sql, Object... paramters) throws SQLException, InterruptedException {
		Object result = null;
		ResultSet rs = null;
		try {
			if(this.readConnection == null) this.readConnection = this.getConnection(this.jdbcDsn + "." + read);
			rs = this.execute(sql, this.readConnection, paramters);
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

	public DBQuery2 setUpdateData(HashMap<String, String> updateData) {
		updateMap.putAll(updateData);
		return this;
	}
	
	private String updateSQL() throws SQLException {
		this.where();
		StringBuilder updateSQL = new StringBuilder("");
		if (updateMap != null && updateMap.size() > 0) {
			updateParamters =  new Object[updateMap.size()];
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

	public int update() throws SQLException, InterruptedException {
		return this.update(this.updateSQL(), updateParamters);
	}

	public int update(String sql) throws SQLException, InterruptedException {
		Object paramter = null;
		return this.update(sql, paramter);
	}
	
	public int update(String sql, ArrayList<Object> paramters) throws SQLException, InterruptedException {
		if(paramters != null && paramters.size() > 0) {
			Object[] paramter = paramters.toArray();
			return this.update(sql, paramter);
		}
		return this.update(sql);
	}
	
	public int update(String sql, Object... paramters) throws SQLException, InterruptedException {
		try {
			if(this.writeConnection == null) this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
			preparedStatement = (PreparedStatement) this.writeConnection.prepareStatement(sql);
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
			
			//logger.info("excuse sql:" + getQueryString(sql, paramters));
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.severe("error sql:" + sql);
			throw new SQLException(e);
		} finally {
		}
	}

	private String insertSQL() throws SQLException {
		this.where();
		StringBuilder insertSQL = new StringBuilder("");
		StringBuilder insertSQLValue = new StringBuilder("");
		if (insertMap != null && insertMap.size() > 0) {
			insertParamters =  new Object[insertMap.size()];
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

	public Object insert() throws SQLException, InterruptedException {
		return this.insert(this.insertSQL(), insertParamters);
	}

	public Object insert(String sql) throws SQLException, InterruptedException {
		Object paramter = null;
		return this.insert(sql, paramter);
	}

	public Object insert(String sql, ArrayList<Object> paramters) throws SQLException, InterruptedException {
		if(paramters != null && paramters.size() > 0) {
			Object[] paramter = paramters.toArray();
			return this.insert(sql, paramter);
		}
		return this.insert(sql);
	}
	
	public Object insert(String sql, Object... paramters) throws SQLException, InterruptedException {
		ResultSet rs = null;
		Object result = null;
		try {
			if(this.writeConnection == null) this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
			preparedStatement = (PreparedStatement) this.writeConnection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
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
			logger.info("excuse sql:" + getQueryString(sql, paramters));
			return result;
		} catch (SQLException e) {
			logger.severe("error sql:" + sql);
			throw new SQLException(e);
		}

	}

	// 保存对象 （未关闭数据库 资源） 表名与类名相同
	public Object insert(Object object) throws Exception {
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
				sql.append(fieldName).append(", ");
				sqlParamters.append("?, ");
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
			if(this.writeConnection == null) this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
			preparedStatement = (PreparedStatement) this.writeConnection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
			for (int i = 1; i <= valueObj.size(); i++) {
				preparedStatement.setObject(i, valueObj.get(i - 1));
			}
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
		return temp.substring(temp.lastIndexOf(".") + 1);
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
		if(this.writeConnection == null) this.writeConnection = this.getConnection(this.jdbcDsn + "." + write);
		try {
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

	private static List<Map<String, Object>> resultSetToListMap(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = null;
		while (rs.next()) {
			if(md == null) md = rs.getMetaData();
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


	/**
	 * 提交事务并关闭数据库连接
	 * 
	 * @throws SQLException
	 */
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

	/**
	 * 
	 * @param c
	 *            for example Person.class
	 * @param primaryKeys
	 *            primaryKeys为主键,参数顺序和表中保持一致 如果id， name 为主键 类名为Person 则
	 *            getEntity(Person.class,1,"name")
	 * @return
	 * @throws InterruptedException 
	 * @throws SQLException 
	 */
	public Object getEntity(Class<?> entityClassT, Object... primaryKeys) throws SQLException, InterruptedException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		DatabaseMetaData dmd = null;
		Object obj = null;// 要返回的对象
		String tableName = entityClassT.getSimpleName().toLowerCase();// person 表的名字
		List<Object> primaryKeyNameList = new ArrayList<Object>();
		Field[] fields = entityClassT.getFields();// 获取所有的属性
		StringBuilder sql = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
		if(this.readConnection == null) this.readConnection = this.getConnection(this.jdbcDsn + "." + read);
		try {
			obj = entityClassT.newInstance();
			dmd = this.readConnection.getMetaData();
			rs = dmd.getPrimaryKeys(null, null, tableName);
			while (rs.next()) {
				sql.append(rs.getObject(4) + "=?");
				sql.append(" and ");
				primaryKeyNameList.add(rs.getObject(4));// 将从表中获取的 主键字段存到 list中，
														// 主键位于表中第几列=rs.getString(5)
			}
			sql.delete(sql.length() - 4, sql.length());
			ps = (PreparedStatement) this.readConnection.prepareStatement(sql.toString());
			for (int l = 0; l < primaryKeyNameList.size(); l++) {
				ps.setObject(l + 1, primaryKeys[l]);
			}
			rs = ps.executeQuery();
			System.out.println(ps.toString().split(":")[1]);
			if (rs.next()) {
				for (int k = 0; k < fields.length; k++) {
					fields[k].set(obj, rs.getObject(k + 1));
				}
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

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
	                t.append("'"+parameterValues[i]+"'");    
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

}
