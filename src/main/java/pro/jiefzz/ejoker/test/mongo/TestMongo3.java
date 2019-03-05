package pro.jiefzz.ejoker.test.mongo;

import static com.mongodb.client.model.Filters.*;

import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class TestMongo3 {

	public static void main(String[] args) {

		MongoClient mongoClient = new MongoClient("192.168.199.123", 27017);

		/*
		 * 选择数据库
		 */
		MongoDatabase mongoDatabase = mongoClient.getDatabase("testE");

		/*
		 * 选择文档集合
		 */
		MongoCollection<Document> mc = mongoDatabase.getCollection("EventStream").withWriteConcern(WriteConcern.MAJORITY);
		
		AtomicInteger ai = new AtomicInteger();
		FindIterable<Document> find = mc.find(in("aggregateRootId", "4cc13ea2c1e85cefb48662455", "cec13ea2c1e85cefb4866e455", "6fc13ea2c1e85cefb48660555", "c3d13ea2c1e85cefb4866a555", "e1d13ea2c1e85cefb48666555", "28d13ea2c1e85cefb4866e555", "40e13ea2c1e85cefb48660755", "4fe13ea2c1e85cefb4866a855", "07023ea2c1e85cefb48666b55", "a7023ea2c1e85cefb4866eb55", "ca023ea2c1e85cefb48668c55", "2f023ea2c1e85cefb4866cd55", "01123ea2c1e85cefb48660e55", "29123ea2c1e85cefb4866ef55", "4c123ea2c1e85cefb48666065", "8d123ea2c1e85cefb48660165", "00223ea2c1e85cefb4866c165", "e1223ea2c1e85cefb48664265", "c2e13ea2c1e85cefb48666755", "8dc13ea2c1e85cefb48666455", "8dc13ea2c1e85cefb48668455", "cec13ea2c1e85cefb4866c455", "e1d13ea2c1e85cefb48664555"));
		find.forEach(new Block<Document>() {
			@Override
			public void apply(Document t) {
				ai.getAndIncrement();
				System.err.println(t.getString("aggregateRootId"));
			}
		});
		
		System.err.println(ai.get());
		
		mongoClient.close();
	}

}
