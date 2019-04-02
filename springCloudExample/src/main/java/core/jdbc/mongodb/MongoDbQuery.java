package core.jdbc.mongodb;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;

public class MongoDbQuery {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private String mongoDbDsn = "test";
	private String table_name = "";
	private Class<?> entityClass = null;
	private BasicDBObject sortDBObject = null;
	private int limit = 0;

	public MongoDbQuery(String dsn) {
		this.mongoDbDsn = dsn;
	}

	public static MongoDbQuery instance(String mongoDbDsn) {
		return new MongoDbQuery(mongoDbDsn);
	}

	/**
	 * 增加对象
	 * 
	 * @param   <T>
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public <T> ObjectId save(Object object) throws Exception {
		Class<?> t = object.getClass();
		String table = this.getClassName(t);
		Document document = new Document();
		Field[] fields = t.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			//if(field.getType().getSimpleName().equals("ObjectId")) continue;
			String fieldName = fields[i].getName();
			String methodNameBegin = fieldName.substring(0, 1).toUpperCase();
			String methodName = "get" + methodNameBegin + fieldName.substring(1);

			Method method = t.getMethod(methodName);
			Object tempObj = method.invoke(object);
			document.put(field.getName(), tempObj);
		}
		return MongoDb.instance(this.mongoDbDsn).insert(table, document);
	}
	
	public <T> boolean saveMostly(Object object) throws Exception {
		Class<?> t = object.getClass();
		String table = this.getClassName(t);
		Document document = new Document();
		Field[] fields = t.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			//if(field.getType().getSimpleName().equals("ObjectId")) continue;
			String fieldName = fields[i].getName();
			String methodNameBegin = fieldName.substring(0, 1).toUpperCase();
			String methodName = "get" + methodNameBegin + fieldName.substring(1);

			Method method = t.getMethod(methodName);
			Object tempObj = method.invoke(object);
			document.put(field.getName(), tempObj);
		}
		return MongoDb.instance(this.mongoDbDsn).insertMostly(table, document);
	}

	/**
	 * 插入一个list集合对象
	 * 
	 * @param      <T>
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */

	public <T> boolean insertAll(Class<T> t, List<T> list) throws Exception {
		String table = this.getClassName(t);
		Field[] fields = t.getDeclaredFields();
		List<Document> documents = new ArrayList<>();
		int length = list.size();
		for(int j = 0; j < length; j++) {
			Document document = new Document();
			T objClass = list.get(j);
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				if(field.getType().getSimpleName().equals("ObjectId")) continue;
				String fieldName = fields[i].getName();
				String methodNameBegin = fieldName.substring(0, 1).toUpperCase();
				String methodName = "get" + methodNameBegin + fieldName.substring(1);

				Method method = t.getMethod(methodName);
				Object tempObj = method.invoke(objClass);
				document.put(field.getName(), tempObj);
			}
			documents.add(document);
		}
		return MongoDb.instance(this.mongoDbDsn).insertMany(table, documents);
	}

	/**
	 * 删除对象
	 * 
	 * @param   <T>
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */

	public <T> boolean delete(Class<T> t, BasicDBObject basicDBObject) throws Exception {
		String table = this.getClassName(t);
		return MongoDb.instance(this.mongoDbDsn).delete(table, basicDBObject);
	}

	/**
	 * 
	 * 根据条件更新数据信息
	 * 
	 * @param <T>
	 * @return
	 * @throws Exception
	 */

	public <T> boolean update(Class<T> t, BasicDBObject whereBasicDBObjectt, BasicDBObject updateBasicDBObject)
			throws Exception {
		String table = this.getClassName(t);
		return MongoDb.instance(this.mongoDbDsn).update(table, whereBasicDBObjectt, updateBasicDBObject);
	}

	/**
	 * 
	 * 查询所有
	 * 
	 * @return
	 * @throws Exception
	 */

	public <T> List<T> findAll(Class<T> t) throws Exception {		
		String table = this.getClassName(t);
		List<T> list = new ArrayList<T>();
		MongoCursor<Document> documentsCursor = MongoDb.instance(this.mongoDbDsn).setSortDBObject(this.sortDBObject).setLimit(this.limit).queryAll(table);
		while (documentsCursor.hasNext()) {
			T objClass = t.newInstance();
			Field[] fields = t.getDeclaredFields();
			Document document = documentsCursor.next();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Object value = document.get(field.getName());
				field.setAccessible(true);
				if(value != null) field.set(objClass, value);
			}
			list.add(objClass);
			if(list.size() > 0) return (List<T>) list;
		}
		return null;
	}

	/**
	 * 
	 * 根据查询 查找list
	 * 
	 * @param query
	 * @throws Exception
	 */

	public <T> List<T> find(Class<T> t, BasicDBObject basicDBObject) throws Exception {
		String table = this.getClassName(t);
		List<T> list = new ArrayList<T>();
		MongoCursor<Document> documentsCursor = MongoDb.instance(this.mongoDbDsn).setSortDBObject(this.sortDBObject).setLimit(this.limit).queryByDocument(table, basicDBObject);
		while (documentsCursor.hasNext()) {
			T objClass = t.newInstance();
			Field[] fields = t.getDeclaredFields();
			Document document = documentsCursor.next();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Object value = document.get(field.getName());
				field.setAccessible(true);
				System.out.println(value);
				if(value != null) field.set(objClass, value);
			}
			list.add(objClass);
			if(list.size() > 0) return (List<T>) list;
		}
		return null;
	}

	public BasicDBObject getSortDBObject() {
		return sortDBObject;
	}

	public MongoDbQuery Sort(BasicDBObject sortDBObject) {
		this.sortDBObject = sortDBObject;
		return this;
	}

	public int getLimint() {
		return limit;
	}

	public MongoDbQuery setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * 
	 * 根据 id 查询对象
	 * 
	 * @param id
	 * 
	 * @return
	 * @throws Exception
	 * 
	 */

	public <T> T findById(Class<T> t, String id) throws Exception {
		String table = this.getClassName(t);
		MongoCursor<Document> documentsCursor = MongoDb.instance(this.mongoDbDsn).queryByID(table, id);
		T objClass = t.newInstance();
		Field[] fields = t.getDeclaredFields();
		while (documentsCursor.hasNext()) {
			Document document = documentsCursor.next();
			System.out.println(document);
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				Object value = document.get(field.getName());
				//Object value2 = document.getList(field.getName().toString(), t);
				field.setAccessible(true);
				if(value != null) field.set(objClass, value);
			}
		}
		return objClass;
	}
	
	public MongoDbQuery table(Class<?> clazz) {
		this.setEntityClass(clazz);
		this.table_name = this.getClassName(clazz);
		return this;
	}

	public MongoDbQuery table(String table_name) {
		this.table_name = table_name;
		return this;
	}

	// 得到类名，不包含包名
	private String getClassName(Class<?> className) {
		String temp = className.getName();
		this.table_name = temp.substring(temp.lastIndexOf(".") + 1).toLowerCase();
		return this.table_name;
	}

	public Class<?> getEntityClass() {
		return entityClass;
	}

	private void setEntityClass(Class<?> entityClass) {
		this.entityClass = entityClass;
	}
	

}
