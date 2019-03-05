package pro.jiefzz.ejoker.test.mongo;

import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TestMongo2 {

	public static void main(String[] args) {

		MongoClient mongoClient = new MongoClient("192.168.199.123", 27017);

		/*
		 * 选择数据库
		 */
		MongoDatabase mongoDatabase = mongoClient.getDatabase("testE");

		/*
		 * 选择文档集合
		 */
		MongoCollection<Document> mc = mongoDatabase.getCollection("PublicedVersionStore").withWriteConcern(WriteConcern.MAJORITY);
		
		FindIterable<Document> find = mc.find(eq("version", 1l));
		find.forEach(new Block<Document>() {
			@Override
			public void apply(Document t) {
				System.err.println(t.getString("aggregateRootId"));
			}
		});
		
		mongoClient.close();
	}

}
