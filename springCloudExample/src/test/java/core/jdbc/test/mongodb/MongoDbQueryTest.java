package core.jdbc.test.mongodb;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import com.Example.model.Entity.Col;
import com.mongodb.BasicDBObject;

import core.jdbc.mongodb.MongoDbQuery;

class MongoDbQueryTest {

	@Test
	void test() {
		try {
			String id = "5c789be27a00128f64ae2a15";
			Col result = MongoDbQuery.instance("mongodb.host1").findById(Col.class, id);
			System.out.println(result.getBy());
			List<Col> lsit = MongoDbQuery.instance("mongodb.host1").findAll(Col.class);
			System.out.println(lsit.get(0).get_id());
			//
			BasicDBObject basicDBObject = new BasicDBObject("title", "MongoDB 教程");
			lsit = MongoDbQuery.instance("mongodb.host1").find(Col.class, basicDBObject);
			if (lsit != null) {
				System.out.println(lsit.get(0).getDescription());
				System.out.println(lsit.get(0).getTags());
			}
			
			BasicDBObject sortDBObject = new BasicDBObject("title",-1);
			int limit = 3;
			lsit = MongoDbQuery.instance("mongodb.host1").Sort(sortDBObject).setLimit(limit).find(Col.class, basicDBObject);
			if (lsit != null) {
				System.out.println(lsit.get(0).getDescription());
			}
			
			Col insertTest = new Col();
			insertTest.set_id(new ObjectId());
			insertTest.setBy("afafasfsadf0000");
			insertTest.setDescription("afdafas");
			insertTest.setLikes(1111);
			List<String> taglist = new ArrayList<>();
			taglist.add("test");
			insertTest.setTags(taglist);
			insertTest.setTitle("232323");
			insertTest.setUrl("sssss");
			ObjectId _id = MongoDbQuery.instance("mongodb.host1").save(insertTest);
			System.out.println(_id);
			System.out.println(new ObjectId());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
