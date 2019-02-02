package pro.jiefzz.ejoker.demo.simple.transfer.completion;

import java.util.ArrayList;
import java.util.List;

import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoDatabase;

//@EService
public class MongoProvider {
	
	private final MongoDatabase mongoDatabase;
	
	private final DB mongoDB;

	public MongoProvider() {

		/*
		 * 选择数据库
		 */
		mongoDatabase = (new MongoClient( "192.168.199.123" , 27017 )).getDatabase("testE");
//
//		//属性文件读取参数信息
//		ResourceBundle bundle = ResourceBundle.getBundle("mongodb");
//		
//		MongoClientOptions.Builder build = new MongoClientOptions.Builder(); 
//		//与目标数据库能够建立的最大connection数量,默认100
//		build.connectionsPerHost(Integer.valueOf(bundle.getString("mongodb.connectionsPerHost")));
//		build.threadsAllowedToBlockForConnectionMultiplier(Integer.valueOf(bundle.getString("mongodb.threadsAllowedToBlockForConnectionMultiplier")));
//		/*
//		 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
//		 * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
//		 * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
//		 */
//		build.maxWaitTime(Integer.valueOf(bundle.getString("mongodb.maxWaitTime")));  
//		//与数据库建立连接的timeout设置为1分钟
//		build.connectTimeout(Integer.valueOf(bundle.getString("mongodb.connectTimeout"))); 
//		build.socketTimeout(Integer.valueOf(bundle.getString("mongodb.socketTimeout")));
//		
		Builder legacyDefaults = MongoClientOptions.builder()
        .legacyDefaults();
		legacyDefaults.connectionsPerHost(8000);
		
		List<ServerAddress> serverList = new ArrayList<>();
		serverList.add(new ServerAddress("192.168.199.123" , 27017));
		
		Mongo mongo = new Mongo(serverList, new MongoOptions(legacyDefaults.build()));
		mongoDB = new DB(mongo, "testE");
		
		mongoDB.setWriteConcern(WriteConcern.ACKNOWLEDGED);
	}
	
//	public MongoCollection<Document> getCollection(String collectionName) {
//		return mongoDatabase.getCollection(collectionName);
//	}
	
	public DBCollection getLegacyCollection(String collectionName) {
		return mongoDB.getCollection(collectionName);
	}
}
