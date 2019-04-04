package core.jdbc.mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class MongoDb {
	private MongoClient mongoClient;
	private MongoDatabase mongoDatabase;
	private static Map<String, String> drivers = new HashMap<String, String>();// 驱动信息
	private static Map<String, MongoDb> instances = new HashMap<String, MongoDb>();
	private BasicDBObject sortDBObject = null;
	private int limit = 0;
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public MongoDb() {
		if (drivers.size() == 0)
			try {
				this.loadDrivers();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public static MongoDb instance(String mongoDbDsn) {
		MongoDb instanceMongoDb = null;
		if (instances.containsKey(mongoDbDsn)) {
			instanceMongoDb = instances.get(mongoDbDsn);
		} else {
			instanceMongoDb = new MongoDb();
			MongoClientOptions.Builder build = new MongoClientOptions.Builder();
			int maxConnect = 2000;
			int maxWaitThread = 20;
			int connectTimeout = 1000;
			int maxWaitTime = 1000;
			int socketTimeout = 1000;
			int serverSelectionTimeout = 1000;
			int heartbeatConnectTimeout = 1000;
			int heartbeatSocketTimeout = 1000;
			//
			build.connectionsPerHost(Integer.valueOf(maxConnect));
			build.threadsAllowedToBlockForConnectionMultiplier(Integer.valueOf(maxWaitThread));
			build.connectTimeout(Integer.valueOf(connectTimeout));// 连接超时时间
			build.maxWaitTime(Integer.valueOf(maxWaitTime));// 最大等待时间
			build.socketTimeout(socketTimeout);// socket 连接超时时间
			build.heartbeatConnectTimeout(heartbeatConnectTimeout);
			build.heartbeatSocketTimeout(heartbeatSocketTimeout);
			build.serverSelectionTimeout(serverSelectionTimeout);// 最大选择超时时间
			// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
			build.threadsAllowedToBlockForConnectionMultiplier(5000);
			build.writeConcern(WriteConcern.ACKNOWLEDGED);
			//
			MongoClientURI uri = new MongoClientURI(drivers.get(mongoDbDsn), build);
			instanceMongoDb.mongoClient = new MongoClient(uri);
			// 连接到数据库
			instanceMongoDb.mongoDatabase = instanceMongoDb.mongoClient.getDatabase(uri.getDatabase());
			// MongoCollection<Document> test = mongoDatabase.getCollection("test");
			instances.put(mongoDbDsn, instanceMongoDb);
		}
		instanceMongoDb.limit = 0;
		instanceMongoDb.sortDBObject = null;
		return instanceMongoDb;
	}

	public void loadDrivers() throws IOException {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config/jdbc.mongo.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		inputStream.close();
		String key = "";
		String value = "";
		Set<Object> set = properties.keySet();
		Iterator<Object> it = set.iterator();
		while (it.hasNext()) {
			key = (String) it.next();
			value = properties.getProperty(key);
			logger.info("loading config: key->" + key + ", value->" + value);
			drivers.put(key, value);
		}
	}

	public MongoCursor<Document> queryByID(String table, String id) throws Exception {
		try {
			MongoCursor<Document> documents = null;
			MongoCollection<Document> collection = mongoDatabase.getCollection(table);
			Document query = new Document();
			query.append("_id", new ObjectId(id));
			FindIterable<Document> iterable = collection.find(query);
			documents = iterable.iterator();
			return documents;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mongo_error");
			logger.error("mongo_error:" + e);
		}
		return null;
	}

	public MongoCursor<Document> queryByDocument(String table, BasicDBObject basicDBObject) throws Exception {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(table);
			FindIterable<Document> iterable = null;
			if(this.sortDBObject != null && this.limit == 0) {
				iterable = collection.find(basicDBObject).sort(this.sortDBObject);
			} else if(this.sortDBObject != null && this.limit > 0) {
				iterable = collection.find(basicDBObject).sort(this.sortDBObject).limit(this.limit);
			} else if(this.sortDBObject == null && this.limit > 0) {
				iterable = collection.find(basicDBObject).limit(this.limit);
			} else {
				iterable = collection.find(basicDBObject);
			}
			MongoCursor<Document> documents = iterable.iterator();
			return documents;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mongo_error");
			logger.error("mongo_error:" + e);
		}
		return null;
	}

	public BasicDBObject getSortDBObject() {
		return sortDBObject;
	}

	public MongoDb setSortDBObject(BasicDBObject sortDBObject) {
		//.sort(new BasicDBObject("create_time",-1));  
		this.sortDBObject = sortDBObject;
		return this;
	}

	public int getLimit() {
		return limit;
	}

	public MongoDb setLimit(int limit) {
		this.limit = limit;
		return this;
	}

	public MongoCursor<Document> queryAll(String table) throws Exception {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(table);
			FindIterable<Document> iterable = null;
			if(this.sortDBObject != null && this.limit == 0) {
				iterable = collection.find().sort(this.sortDBObject);
			} else if(this.sortDBObject != null && this.limit > 0) {
				iterable = collection.find().sort(this.sortDBObject).limit(this.limit);
			} else if(this.sortDBObject == null && this.limit > 0) {
				iterable = collection.find().limit(this.limit);
			} else {
				iterable = collection.find();
			}
			MongoCursor<Document> documents = iterable.iterator();
			return documents;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mongo_error");
			logger.error("mongo_error:" + e);
		}
		return null;
	}

	public List<Document> findIterable(FindIterable<Document> iterable) throws Exception {
		try {
			List<Document> list = new ArrayList<Document>();
			MongoCursor<Document> cursor = iterable.iterator();
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				list.add(doc);
			}
			cursor.close();
			return list;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mongo_error");
			logger.error("mongo_error:" + e);
		}
		return null;
	}

	public ObjectId insert(String table, Document document) throws Exception {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(table);
			collection.insertOne(document);
			ObjectId id = (ObjectId)document.get("_id");
			return id;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mongo_error");
			logger.error("mongo_error:" + e);
		}
		return null;
	}
	
	public boolean insertMostly(String table, Document document) throws Exception {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(table);
			collection.insertOne(document);
			return true;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mongo_error");
			logger.error("mongo_error:" + e);
		}
		return false;
	}

	public boolean insertMany(String table, List<Document> documents) throws Exception {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(table);
			long preCount = collection.countDocuments();
			collection.insertMany(documents);
			long nowCount = collection.countDocuments();
			if ((nowCount - preCount) == documents.size()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			MDC.put("APP_NAME", "mongo_error");
			logger.error("mongo_error:" + e);
		}
		return false;
	}

	public boolean delete(String table, BasicDBObject basicDBObject) throws Exception {
		try {
		MongoCollection<Document> collection = mongoDatabase.getCollection(table);
		DeleteResult deleteManyResult = collection.deleteMany(basicDBObject);
		long deletedCount = deleteManyResult.getDeletedCount();
		if (deletedCount > 0) {
			return true;
		} else {
			return false;
		}
		} catch (Exception e) {
			MDC.put("APP_NAME", "mongo_error");
			logger.error("mongo_error:" + e);
		}
		return false;
	}

	public boolean update(String table, BasicDBObject whereBasicDBObjectt, BasicDBObject updateBasicDBObject)
			throws Exception {
		try {
			MongoCollection<Document> collection = mongoDatabase.getCollection(table);
			UpdateResult updateManyResult = collection.updateMany(whereBasicDBObjectt,
					new Document("$set", updateBasicDBObject));
			long modifiedCount = updateManyResult.getModifiedCount();
			if (modifiedCount > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			MDC.put("APP_NAME", "mongo_error");
			logger.error("mongo_error:" + e);
		}
		return false;
	}

	public void createCollection(String table) throws Exception {
		mongoDatabase.createCollection(table);
	}

	public void dropCollection(String table) throws Exception {
		mongoDatabase.getCollection(table).drop();
	}

}
