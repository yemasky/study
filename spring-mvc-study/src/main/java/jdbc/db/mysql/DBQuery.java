package jdbc.db.mysql;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils.Null;

public class DBQuery {
	private String table_name = "";
	private String primary_key = "id";
	private String field = "*";
	private Connection connection = null;
	private static PreparedStatement preparedStatement = null;
    private static CallableStatement callableStatement = null;
    
	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public String getPrimary_key() {
		return primary_key;
	}

	public void setPrimary_key(String primary_key) {
		this.primary_key = primary_key;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public List getList(String sql) throws SQLException {
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
            rs = preparedStatement.executeQuery();
 
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
 
    }
	
	public List query(String sql, Object... paramters)
            throws SQLException {
 
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
 
            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            rs = preparedStatement.executeQuery();
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
	
	public Object getSingle(String sql) throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
 
    }
	
	public Object getSingle(String sql, Object... paramters)
            throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getPreparedStatement(sql);
 
            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
	
	public int update(String sql) throws SQLException {
		 
        try {
            getPreparedStatement(sql);
 
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }
	
	public int update(String sql, Object... paramters)
            throws SQLException {
        try {
            getPreparedStatement(sql);
 
            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }
 
	public Object insertWithReturnPrimeKey(String sql)
            throws SQLException {
        ResultSet rs = null;
        Object result = null;
        try {
            preparedStatement = connection.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.execute();
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }
	
	
	
	public Object insertWithReturnPrimeKey(String sql,
            Object... paramters) throws SQLException {
        ResultSet rs = null;
        Object result = null;
        try {
            preparedStatement = connection.prepareStatement(sql,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            for (int i = 0; i < paramters.length; i++) {
                preparedStatement.setObject(i + 1, paramters[i]);
            }
            preparedStatement.execute();
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        }
 
    }
	
	public List callableQuery(String procedureSql) throws SQLException {
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
            rs = callableStatement.executeQuery();
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
 
	
	 /**
     * 调用存储过程（带参数）,执行查询
     * 
     * @param procedureSql
     *            存储过程
     * @param paramters
     *            参数表
     * @return
     * @throws SQLException
     */
	public List callableQuery(String procedureSql, Object... paramters)
            throws SQLException {
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
 
            for (int i = 0; i < paramters.length; i++) {
                callableStatement.setObject(i + 1, paramters[i]);
            }
            rs = callableStatement.executeQuery();
            return ResultToListMap(rs);
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
	
	/**
     * 调用存储过程，查询单个值
     * 
     * @param procedureSql
     * @return
     * @throws SQLException
     */
    public Object callableGetSingle(String procedureSql)
            throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
            rs = callableStatement.executeQuery();
            while (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
 
    /**
     * 调用存储过程(带参数)，查询单个值
     * 
     * @param procedureSql
     * @param parameters
     * @return
     * @throws SQLException
     */
    public Object callableGetSingle(String procedureSql,
            Object... paramters) throws SQLException {
        Object result = null;
        ResultSet rs = null;
        try {
            getCallableStatement(procedureSql);
 
            for (int i = 0; i < paramters.length; i++) {
                callableStatement.setObject(i + 1, paramters[i]);
            }
            rs = callableStatement.executeQuery();
            while (rs.next()) {
                result = rs.getObject(1);
            }
            return result;
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free(rs);
        }
    }
 
    public Object callableWithParamters(String procedureSql)
            throws SQLException {
        try {
            getCallableStatement(procedureSql);
            callableStatement.registerOutParameter(0, Types.OTHER);
            callableStatement.execute();
            return callableStatement.getObject(0);
 
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
 
    }
 
    /**
     * 调用存储过程，执行增删改
     * 
     * @param procedureSql
     *            存储过程
     * @return 影响行数
     * @throws SQLException
     */
    public int callableUpdate(String procedureSql) throws SQLException {
        try {
            getCallableStatement(procedureSql);
            return callableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }
 
    /**
     * 调用存储过程（带参数），执行增删改
     * 
     * @param procedureSql
     *            存储过程
     * @param parameters
     * @return 影响行数
     * @throws SQLException
     */
    public int callableUpdate(String procedureSql, Object... parameters)
            throws SQLException {
        try {
            getCallableStatement(procedureSql);
            for (int i = 0; i < parameters.length; i++) {
                callableStatement.setObject(i + 1, parameters[i]);
            }
            return callableStatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            free();
        }
    }
 
    /**
     * 批量更新数据
     * 
     * @param sqlList
     *            一组sql
     * @return
     */
    public int[] batchUpdate(List<String> sqlList) {
 
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
            free(statenent, null);
        }
        return result;
    }
 
    private static List ResultToListMap(ResultSet rs) throws SQLException {
        List list = new ArrayList();
        while (rs.next()) {
            ResultSetMetaData md = rs.getMetaData();
            Map map = new HashMap();
            for (int i = 1; i < md.getColumnCount(); i++) {
                map.put(md.getColumnLabel(i), rs.getObject(i));
            }
            list.add(map);
        }
        return list;
    }
 
    /**
     * 获取PreparedStatement
     * 
     * @param sql
     * @throws SQLException
     */
    private void getPreparedStatement(String sql) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);
    }
 
    /**
     * 获取CallableStatement
     * 
     * @param procedureSql
     * @throws SQLException
     */
    private void getCallableStatement(String procedureSql)
            throws SQLException {
        callableStatement = connection.prepareCall(procedureSql);
    }
 
    /**
     * 释放资源
     * 
     * @param rs
     *            结果集
     */
    public static void free(ResultSet rs) {
 
        JdbcUnits.free(conn, preparedStatement, rs);
    }
 
    /**
     * 释放资源
     * 
     * @param statement
     * @param rs
     */
    public static void free(Statement statement, ResultSet rs) {
        JdbcUnits.free(conn, statement, rs);
    }
 
    /**
     * 释放资源
     */
    public static void free() {
 
        free(null);
    }
    
    /** 
     * 提交事务并关闭数据库连接 
     */  
    public void commit() {  
        try {  
            if (!autoCommit) {  
                connection.commit();  
            }  
        } catch (SQLException e) {  
            throw new RuntimeException(e);  
        } finally {  
            autoCommit = true;  
            closeAll();  
        }  
    }  
  
    /** 
     * 回滚事务并关闭数据库连接 
     */  
    public void rollback() {  
        try {  
            if (!autoCommit) {  
                connection.rollback();  
            }  
        } catch (SQLException e) {  
            throw new RuntimeException(e);  
        } finally {  
            autoCommit = true;  
            closeAll();  
        }  
    }  
 
    /**
	 * 
	 * @param c
	 *            for example Person.class
	 * @param primaryKeys
	 *            primaryKeys为主键,参数顺序和表中保持一致 如果id， name 为主键 类名为Person 则 getEntity(Person.class,1,"name")
	 * @return
	 */
	public Object getEntity(Class c, Object... primaryKeys) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		DatabaseMetaData dmd = null;
		Object obj = null;// 要返回的对象
		String tableName = c.getSimpleName().toLowerCase();// person 表的名字
		List primaryKeyNameList = new ArrayList();
		Field[] fields = c.getFields();// 获取所有的属性
		StringBuilder sql = new StringBuilder("select * from " + tableName
				+ " where ");
		try {
			obj = c.newInstance();
			dmd = connection.getMetaData();
			rs = dmd.getPrimaryKeys(null, null, tableName);
			while (rs.next()) {
				sql.append(rs.getObject(4) + "=?");
				sql.append(" and ");
				primaryKeyNameList.add(rs.getObject(4));// 将从表中获取的 主键字段存到 list中， 主键位于表中第几列=rs.getString(5)
			}
			sql.delete(sql.length() - 4, sql.length());
			ps = (PreparedStatement) connection.prepareStatement(
					sql.toString());
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
			rs=null;
			ps=null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return obj;
	}
}
