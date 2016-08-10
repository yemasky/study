package jdbc.db.mysql;

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

public class DBQuery extends ConnectionPoolManager {
	private final Logger logger = Logger.getLogger("jdbc.db.mysql.DBQuery");

	public DBQuery() throws SQLException, IOException {
		super();
	}

	public String table_name = "";
	public String primary_key = "id";
	public String field = "*";
	private String where = "";
	private String order = "";
	private String group = "";
	private String limit = "";
	private String insertType = "INTO";
	private Class<?> classEntity = null;
	private Map<String, Object> whereMap = null;
	private List<Object> whereParamters = null;
	private Map<String, Object> updateMap = null;
	private List<Object> updateParamters = null;
	private Map<String, Object> insertMap = null;
	private List<Object> insertParamters = null;

	private Connection connection = null;
	private PreparedStatement preparedStatement = null;

	public DBQuery setTable(String table_name) {
		this.table_name = table_name;
		return this;
	}

	public DBQuery setTable(Class<?> className) {
		this.classEntity = className;
		this.table_name = this.getClassName(className);
		return this;
	}

	public DBQuery setInsertType(String type) {
		this.insertType = type;
		return this;
	}

	public DBQuery setPrimaryKey(String primary_key) {
		this.primary_key = primary_key;
		return this;
	}

	public DBQuery setConnection(Connection connection) {
		this.connection = connection;
		return this;
	}

	public DBQuery where(String whereSQL) {
		if (this.where.startsWith(" WHERE")) {
			this.where = this.where + " AND " + whereSQL;
		} else {
			this.where = " WHERE " + whereSQL;
		}
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
			this.order = "  ORDER BY" + orderSQL;
		}
		return this;
	}

	public DBQuery limit(int offset, int rows) {
		this.limit = " LIMIT " + offset + ", " + rows + ";";
		return this;
	}

	public DBQuery where(HashMap<String, String> whereData) {
		whereMap.putAll(whereData);
		return this;
	}

	private void where() {
		if (whereMap != null) {
			whereParamters = null;
			StringBuilder whereSQL = new StringBuilder("");
			for (String key : whereMap.keySet()) {
				if (whereSQL.equals("")) {
					whereSQL.append(key + " = ? ");
				} else {
					whereSQL.append(" AND " + key + " = ? ");
				}
				whereParamters.add(whereMap.get(key));
			}
			if (this.where.startsWith(" WHERE")) {
				this.where = this.where + " AND " + whereSQL.toString();
			} else {
				this.where = " WHERE " + whereSQL.toString();
			}
		}
	}

	public DBQuery setUpdateData(HashMap<String, String> updateData) {
		updateMap.putAll(updateData);
		return this;
	}

	public DBQuery setInsertData(HashMap<String, String> insertData) {
		insertMap.putAll(insertData);
		return this;
	}

	private String sql() {
		this.where();
		String sql = "SELECT " + this.field + " FROM " + this.table_name + this.where + this.order + this.group
				+ this.limit;
		return sql;
	}

	public List<Map<String, Object>> getList() throws SQLException {
		return this.getList(this.sql(), whereParamters);
	}

	public List<Map<String, Object>> getList(String sql) throws SQLException {
		return this.getList(this.sql(), new Object());
	}

	public List<Map<String, Object>> getList(String sql, Object... paramters) throws SQLException {
		ResultSet rs = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (paramters != null && paramters.length > 0) {
				for (int i = 0; i < paramters.length; i++) {
					preparedStatement.setObject(i + 1, paramters[i]);
				}
			}
			rs = preparedStatement.executeQuery();
			return resultSetToListMap(rs);
		} catch (SQLException e) {
			logger.severe("error sql:" + sql);
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
    public <T> List<T> getList(Class<T> cls, String sql) throws SQLException {
        List<T> list = new ArrayList<T>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                T obj = executeResultSet(cls, rs);
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
    
	public Object getOne() throws SQLException {
		return this.getOne(this.sql(), whereParamters);
	}

	public Object getOne(String sql) throws SQLException {
		return this.getOne(sql, new Object());
	}

	public Object getOne(String sql, Object... paramters) throws SQLException {
		Object result = null;
		ResultSet rs = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (paramters != null && paramters.length > 0) {
				for (int i = 0; i < paramters.length; i++) {
					preparedStatement.setObject(i + 1, paramters[i]);
				}
			}
			rs = preparedStatement.executeQuery();
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

	private String updateSQL() throws SQLException {
		this.where();
		StringBuilder updateSQL = new StringBuilder("");
		if (updateMap != null) {
			updateParamters = null;
			for (String key : updateMap.keySet()) {
				if (updateSQL.equals("")) {
					updateSQL.append(key + " = ? ");
				} else {
					updateSQL.append(", " + key + " = ? ");
				}
				updateParamters.add(updateMap.get(key));
			}
		}
		if (updateSQL.equals(""))
			throw new SQLException("Update SQL paramters error");
		String sql = "UPDATE " + this.table_name + " SET " + updateSQL.toString() + this.where;
		return sql;
	}

	public int update() throws SQLException {
		return this.update(this.updateSQL(), updateParamters);
	}

	public int update(String sql) throws SQLException {
		return this.update(sql, new Object());
	}

	public int update(String sql, Object... paramters) throws SQLException {
		try {
			preparedStatement = connection.prepareStatement(sql);
			if (paramters != null && paramters.length > 0) {
				for (int i = 0; i < paramters.length; i++) {
					preparedStatement.setObject(i + 1, paramters[i]);
				}
			}
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
		if (insertMap != null) {
			insertParamters = null;
			for (String key : insertMap.keySet()) {
				if (insertSQL.equals("")) {
					insertSQL.append(key);
					insertSQLValue.append("?");
				} else {
					insertSQL.append(", " + key);
					insertSQLValue.append(", ?");
				}
				insertParamters.add(insertMap.get(key));
			}
		}
		if (insertSQL.equals(""))
			throw new SQLException("insert SQL paramters error");
		String sql = "INSERT " + this.insertType + " " + this.table_name + " ( " + insertSQL.toString() + ") VALUES ("
				+ insertSQLValue.toString() + ")";
		return sql;
	}

	public Object insert() throws SQLException {
		return this.insert(this.insertSQL(), insertParamters);
	}

	public Object insert(String sql) throws SQLException {
		return this.insert(sql, new Object());
	}

	public Object insert(String sql, Object... paramters) throws SQLException {
		ResultSet rs = null;
		Object result = null;
		try {
			preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
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
		StringBuffer sqlAfter = new StringBuffer("");
		for (int i = 0; i < fields.length; i++) {
			String fieldName = fields[i].getName();
			String methodNameBegin = fieldName.substring(0, 1).toUpperCase();
			String methodName = "get" + methodNameBegin + fieldName.substring(1);

			Method method = objectClass.getMethod(methodName);
			Object tempObj = method.invoke(object);
			if (tempObj != null) {
				valueObj.add(tempObj);
				sql.append(fieldName).append(", ");
				sqlAfter.append("?, ");
			}
		}
		if (valueObj.size() == 0) {
			return 0;
		} else {
			// 最后一位为,，去除
			if (sql.charAt(sql.length() - 1) == ',') {
				sql.deleteCharAt(sql.length() - 1);
			}
			if (sqlAfter.charAt(sqlAfter.length() - 1) == ',') {
				sqlAfter.deleteCharAt(sqlAfter.length() - 1);
			}
			sql.append(") VALUES (").append(sqlAfter).append(")");
			preparedStatement = connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS);
			for (int i = 1; i <= valueObj.size(); i++) {
				preparedStatement.setObject(i, valueObj.get(i - 1));
			}
			// pstat.executeUpdate();
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
	 */
	public int[] batchUpdate(List<String> sqlList) throws SQLException {
		int[] result = new int[] {};
		Statement statenent = null;
		try {
			connection.setAutoCommit(false);
			statenent = connection.createStatement();
			for (String sql : sqlList) {
				statenent.addBatch(sql);
			}
			result = statenent.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
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
		while (rs.next()) {
			ResultSetMetaData md = rs.getMetaData();
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
		connection.commit();
	}

	/**
	 * 回滚事务并关闭数据库连接
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		connection.rollback();
	}

	/**
	 * 
	 * @param c
	 *            for example Person.class
	 * @param primaryKeys
	 *            primaryKeys为主键,参数顺序和表中保持一致 如果id， name 为主键 类名为Person 则
	 *            getEntity(Person.class,1,"name")
	 * @return
	 */
	public Object getEntity(Class<?> c, Object... primaryKeys) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		DatabaseMetaData dmd = null;
		Object obj = null;// 要返回的对象
		String tableName = c.getSimpleName().toLowerCase();// person 表的名字
		List<Object> primaryKeyNameList = new ArrayList<Object>();
		Field[] fields = c.getFields();// 获取所有的属性
		StringBuilder sql = new StringBuilder("select * from " + tableName + " where ");
		try {
			obj = c.newInstance();
			dmd = connection.getMetaData();
			rs = dmd.getPrimaryKeys(null, null, tableName);
			while (rs.next()) {
				sql.append(rs.getObject(4) + "=?");
				sql.append(" and ");
				primaryKeyNameList.add(rs.getObject(4));// 将从表中获取的 主键字段存到 list中，
														// 主键位于表中第几列=rs.getString(5)
			}
			sql.delete(sql.length() - 4, sql.length());
			ps = (PreparedStatement) connection.prepareStatement(sql.toString());
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
			rs = null;
			ps = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return obj;
	}
}
