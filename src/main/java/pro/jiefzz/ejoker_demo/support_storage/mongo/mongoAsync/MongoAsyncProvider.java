package pro.jiefzz.ejoker_demo.support_storage.mongo.mongoAsync;
//package pro.jiefzz.ejoker.demo.completion;
//
//import static java.util.Arrays.asList;
//
//import org.bson.Document;
//
//import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
//import com.jiefzz.ejoker.z.common.context.annotation.context.EInitialize;
//import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
//import com.jiefzz.ejoker.z.common.scavenger.Scavenger;
//import com.mongodb.ServerAddress;
//import com.mongodb.WriteConcern;
//import com.mongodb.async.client.MongoClient;
//import com.mongodb.async.client.MongoClientSettings;
//import com.mongodb.async.client.MongoClients;
//import com.mongodb.async.client.MongoCollection;
//import com.mongodb.async.client.MongoDatabase;
//import com.mongodb.connection.ClusterSettings;
//
//import co.paralleluniverse.fibers.Suspendable;
//
//@EService
//public class MongoAsyncProvider {
//
//	@Dependence
//	private Scavenger scavenger;
//	
//	private MongoDatabase mongoDatabase = null;
//	
//	@EInitialize
//	private void init() {
//		
//		// 使用MongoClientSettings
//		ClusterSettings clusterSettings = ClusterSettings.builder()
//                .hosts(asList(
//                    new ServerAddress("192.168.199.123", 27017)
//                    ))
//                .maxWaitQueueSize(Integer.MAX_VALUE)
//                .build();
//
//		MongoClientSettings settings = MongoClientSettings.builder()
//		                .clusterSettings(clusterSettings).build();
//		MongoClient mongoClient = MongoClients.create(settings);
//
//		mongoDatabase = mongoClient.getDatabase("testE").withWriteConcern(WriteConcern.ACKNOWLEDGED);
//
//        scavenger.addFianllyJob(() -> {
//        	if(null != mongoClient) {
//        		mongoClient.close();
//        	}
//        });
//	}
//
//	@Suspendable
//	public MongoCollection<Document> getCollection(String collectionName) {
//		return mongoDatabase.getCollection(collectionName);
//	}
//	
//}
