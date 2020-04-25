package pro.jiefzz.eden.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.WriteConcernError;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.WriteModel;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.SuspendableRunnable;

public class TestMongo4 {

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
		

		List<WriteModel<? extends Document>> requests = new ArrayList<>();
		
		requests.add(new InsertOneModel<>(new Document("_id", 4)));
		requests.add(new InsertOneModel<>(new Document("_id", 5)));
		requests.add(new InsertOneModel<>(new Document("_id", 6)));
		
		try {
			mc.bulkWrite(requests, new BulkWriteOptions().ordered(false));
		} catch (MongoBulkWriteException e) {
			List<BulkWriteError> writeErrors = e.getWriteErrors();
			for(BulkWriteError bwe : writeErrors) {
				String message = bwe.getMessage();
				System.err.println(message);
			}
			WriteConcernError writeConcernError = e.getWriteConcernError();
			System.err.println(writeConcernError.getMessage());
			e.printStackTrace();
		}
		
		mongoClient.close();
	}

}
