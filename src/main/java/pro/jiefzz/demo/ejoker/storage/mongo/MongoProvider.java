package pro.jiefzz.demo.ejoker.storage.mongo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import co.paralleluniverse.fibers.Suspendable;
import pro.jk.ejoker.common.context.annotation.context.Dependence;
import pro.jk.ejoker.common.context.annotation.context.EInitialize;
import pro.jk.ejoker.common.context.annotation.context.EService;
import pro.jk.ejoker.common.service.Scavenger;
import pro.jk.ejoker.common.system.functional.IFunction;
import pro.jk.ejoker.common.system.functional.IVoidFunction;
import pro.jk.ejoker.common.system.wrapper.MixedThreadPoolExecutor;

@EService
public class MongoProvider {

	@Dependence
	private Scavenger scavenger;

	private MongoDatabase mongoDatabase;

//	private final DB mongoDB;

	private ThreadPoolExecutor threadPoolExecutor = null;

	@EInitialize
	private void init() {

		MongoClient mongoClient;

		MongoClientOptions.Builder build = new MongoClientOptions.Builder();
		build.connectionsPerHost(200); // 与目标数据库能够建立的最大connection数量为50
		build.threadsAllowedToBlockForConnectionMultiplier(1024); // 如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待
		/*
		 * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟
		 * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception
		 * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败
		 */
		build.maxWaitTime(1000 * 60 * 2);
		build.connectTimeout(1000 * 60 * 1); // 与数据库建立连接的timeout设置为1分钟
		build.socketTimeout(1000 * 60 * 1);

		List<ServerAddress> serverList = new ArrayList<>();
		serverList.add(new ServerAddress("test_sit_1", 27017));

		try {
			// 数据库连接实例
			mongoClient = new MongoClient(serverList, build.build());
		} catch (MongoException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		mongoDatabase = mongoClient.getDatabase("testE").withWriteConcern(WriteConcern.ACKNOWLEDGED);

		/*
		 * 选择数据库
		 */
//		mongoDatabase = (new MongoClient( "test_sit_1" , 27017 )).getDatabase("testE");
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
//		Builder legacyDefaults = MongoClientOptions.builder()
//        .legacyDefaults();
//		legacyDefaults.connectionsPerHost(8000);
//		
//		List<ServerAddress> serverList = new ArrayList<>();
//		serverList.add(new ServerAddress("test_sit_1" , 27017));
//		
//		Mongo mongo = new Mongo(serverList, new MongoOptions(legacyDefaults.build()));
//		mongoDB = new DB(mongo, "testE");
//		
//		mongoDB.setWriteConcern(WriteConcern.ACKNOWLEDGED);

		threadPoolExecutor = new MixedThreadPoolExecutor(64, 64, 0l, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {

					private final AtomicInteger threadIndex = new AtomicInteger(0);

					private final ThreadGroup group;

					private final String namePrefix;

					{

						SecurityManager s = System.getSecurityManager();
						group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
						namePrefix = "EJokerMongoPersists-thread-";
					}

					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(null, r, namePrefix + threadIndex.getAndIncrement(), 0);
						if (t.isDaemon())
							t.setDaemon(false);
						if (t.getPriority() != Thread.NORM_PRIORITY)
							t.setPriority(Thread.NORM_PRIORITY);

						return t;
					}

				});

		// 初始化全部存储线程
		threadPoolExecutor.prestartAllCoreThreads();

		scavenger.addFianllyJob(() -> {
			if (null != mongoClient) {
				mongoClient.close();
			}
			if (null != threadPoolExecutor) {
				threadPoolExecutor.shutdown();
			}
		});
	}

	public MongoCollection<Document> getCollection(String collectionName) {
		return mongoDatabase.getCollection(collectionName);
	}

//	public DBCollection getLegacyCollection(String collectionName) {
//		return mongoDB.getCollection(collectionName);
//	}

	@Suspendable
	public <T> Future<T> submitWithInnerExector(IFunction<T> vf) {
		
//		RipenFuture<T> r = new RipenFuture<>();
//		try {
//			r.trySetResult(vf.trigger());
//		} catch (Exception e) {
//			r.trySetException(e);
//		}
//		return r;
		
		return threadPoolExecutor.submit(vf::trigger);
	}

	@Suspendable
	public Future<?> submitWithInnerExector(IVoidFunction vf) {
		
//		RipenFuture<?> r = new RipenFuture<>();
//		try {
//			vf.trigger();
//			r.trySetResult(null);
//		} catch (Exception e) {
//			r.trySetException(e);
//		}
//		return r;
		
		return threadPoolExecutor.submit(vf::trigger);
	}
}
