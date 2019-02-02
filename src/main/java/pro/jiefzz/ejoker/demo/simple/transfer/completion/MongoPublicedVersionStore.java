package pro.jiefzz.ejoker.demo.simple.transfer.completion;

import com.jiefzz.ejoker.infrastructure.IPublishedVersionStore;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.io.AsyncTaskResult;
import com.jiefzz.ejoker.z.common.io.AsyncTaskStatus;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.EJokerFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.RipenFuture;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

//@EService
public class MongoPublicedVersionStore implements IPublishedVersionStore {

	@Dependence
	MongoProvider mongoProvider;
	
	@Override
	public SystemFutureWrapper<AsyncTaskResult<Void>> updatePublishedVersionAsync(String processorName,
			String aggregateRootTypeName, String aggregateRootId, long publishedVersion) {
		try {
			updatePublishedVersion(processorName, aggregateRootTypeName, aggregateRootId, publishedVersion);
			return EJokerFutureWrapperUtil.createCompleteFutureTask();
		} catch (Exception e) {
			RipenFuture<AsyncTaskResult<Void>> ripenFuture = new RipenFuture<>();
			ripenFuture.trySetResult(new AsyncTaskResult<>(AsyncTaskStatus.Failed, e.getMessage(), null));
			return new SystemFutureWrapper<>(ripenFuture);
		}
	}

	@Override
	public SystemFutureWrapper<AsyncTaskResult<Long>> getPublishedVersionAsync(String processorName,
			String aggregateRootTypeName, String aggregateRootId) {
		try {
			long r = getPublishedVersion(processorName, aggregateRootTypeName, aggregateRootId);
			return EJokerFutureWrapperUtil.createCompleteFutureTask(r);
		} catch (Exception e) {
			RipenFuture<AsyncTaskResult<Long>> ripenFuture = new RipenFuture<>();
			ripenFuture.trySetResult(new AsyncTaskResult<>(AsyncTaskStatus.Failed, e.getMessage(), null));
			return new SystemFutureWrapper<>(ripenFuture);
		}
	}

	@Override
	public void updatePublishedVersion(String processorName, String aggregateRootTypeName, String aggregateRootId,
			long publishedVersion) {
		
		DBCollection legacyCollection = mongoProvider.getLegacyCollection(aggregateRootTypeName+"_"+processorName.trim());
		if(0 == 1 - publishedVersion) {
			
		}
		
		
		WriteResult writeResult;
		if(publishedVersion - 1l == 0) {
			writeResult = legacyCollection.update(
				new BasicDBObject("id", aggregateRootId),
				new BasicDBObject("$set", new BasicDBObject("version", publishedVersion)),
				true,
				false
				);
		} else {
			writeResult = legacyCollection.update(
				new BasicDBObject("id", aggregateRootId).append("version", publishedVersion-1l),
				new BasicDBObject("$inc", new BasicDBObject("version", 1)),
				false,
				false
				);
		}
		if(writeResult.getN() - 1 != 0) {
			// 抛错？
		}
		
//		MongoCollection<Document> collection = mongoProvider.getCollection(aggregateRootTypeName+"_"+processorName.trim());
//		if(0 == 1 - publishedVersion) {
//			collection.insertOne(new Document("id", aggregateRootId).append("version", 1l));
//		} else {
//			UpdateResult updateOne = collection.updateOne(new Document("id", aggregateRootId).append("version", publishedVersion-1l),
//					new Document("$set", new Document("version", publishedVersion)), new UpdateOptions().upsert(false));
//			long modifiedCount = updateOne.getModifiedCount();
//			if(0!= 1l - modifiedCount)
//				throw new RuntimeException("Fuck!!!");
//		}
		
	}

	@Override
	public long getPublishedVersion(String processorName, String aggregateRootTypeName, String aggregateRootId) {
		long r = 0;
		{
			DBCollection legacyCollection = mongoProvider.getLegacyCollection(aggregateRootTypeName+"_"+processorName.trim());
			DBObject result = legacyCollection.findOne(new BasicDBObject("id", aggregateRootId));
			if(null != result) {
				r = (Long )result.get("version");
			}
		}
//		{
//			MongoCollection<Document> collection = mongoProvider.getCollection(aggregateRootTypeName+"_"+processorName.trim());
//			FindIterable<Document> sort = collection.find(new Document("id", aggregateRootId)).sort(new Document("version", -1));
//			Document target = sort.first();
//			if(null != target) {
//				r = target.getLong("version");
//			}
//		}
		return r;
	}

}
