package pro.jiefzz.ejoker_demo.support_storage.mongo.mongoSync;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static pro.jiefzz.ejoker.common.system.extension.LangUtil.await;

import java.io.IOException;
import java.util.concurrent.Future;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.common.system.extension.acrossSupport.EJokerFutureTaskUtil;
import pro.jiefzz.ejoker.common.system.extension.acrossSupport.RipenFuture;
import pro.jiefzz.ejoker.common.system.task.AsyncTaskResult;
import pro.jiefzz.ejoker.common.system.task.AsyncTaskStatus;
import pro.jiefzz.ejoker.common.system.task.io.IOExceptionOnRuntime;
import pro.jiefzz.ejoker.eventing.qeventing.IPublishedVersionStore;

@EService
public class MongoPublicedVersionStore implements IPublishedVersionStore {
	
	private final static Logger logger = LoggerFactory.getLogger(MongoPublicedVersionStore.class);

	@Dependence
	private MongoProvider mongoProvider;

	/**
	 * db.PublicedVersionStore.createIndex({"aggregateRootId":1, "processorName":1, "version":1}, {backgroud: true, unique:true})
	 * db.PublicedVersionStore.getIndexes()
	 */
	private final String aggregateVersionIndexName = "aggregateRootId_1_processorName_1_version_1";
	
	private final String collectionNameOfPublishedVersionStore = "PublicedVersionStore";

	@Suspendable
	@Override
	public Future<AsyncTaskResult<Void>> updatePublishedVersionAsync(String processorName,
			String aggregateRootTypeName, String aggregateRootId, long publishedVersion) {
		try {
			await(mongoProvider.submitWithInnerExector(() -> updatePublishedVersion(processorName, aggregateRootTypeName, aggregateRootId, publishedVersion)));
			return EJokerFutureTaskUtil.completeTask();
		} catch (Exception e) {
			RipenFuture<AsyncTaskResult<Void>> ripenFuture = new RipenFuture<>();
			ripenFuture.trySetResult(new AsyncTaskResult<>(AsyncTaskStatus.Failed, e.getMessage(), null));
			return ripenFuture;
		}
	}
	
	@Suspendable
	@Override
	public Future<AsyncTaskResult<Long>> getPublishedVersionAsync(String processorName,
			String aggregateRootTypeName, String aggregateRootId) {
		try {
			long r = await(mongoProvider.submitWithInnerExector(() -> getPublishedVersion(processorName, aggregateRootTypeName, aggregateRootId)));
			return EJokerFutureTaskUtil.completeTask(r);
		} catch (Exception e) {
			RipenFuture<AsyncTaskResult<Long>> ripenFuture = new RipenFuture<>();
			ripenFuture.trySetResult(new AsyncTaskResult<>(AsyncTaskStatus.Failed, e.getMessage(), null));
			return ripenFuture;
		}
	}
	
	private void updatePublishedVersion(String processorName, String aggregateRootTypeName, String aggregateRootId, long publishedVersion) {
		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfPublishedVersionStore);
		long currentTimeMillis = System.currentTimeMillis();
		if(0 == 1 - publishedVersion) {
			try {
				collection.insertOne(new Document("aggregateRootId", aggregateRootId)
						.append("version", 1l)
						.append("processorName", processorName)
						.append("aggregateRootTypeName", aggregateRootTypeName)
						.append("createdOn", currentTimeMillis)
						.append("updatedOn", currentTimeMillis)
						);
			} catch(MongoWriteException e) {
				String message = e.getMessage();
				if(null != message && message.startsWith("E11000 duplicate key")) {
					if(message.contains(aggregateVersionIndexName))
						return;
				}
                logger.error("Insert aggregate published version has storage exception.", e);
				throw new IOExceptionOnRuntime(new IOException(e));
			} catch(RuntimeException e) {
				logger.error("Insert aggregate published version has unknown exception.", e);
				throw e;
			}
		} else {
			try {
				collection.updateOne(
						and(
								eq("aggregateRootId", aggregateRootId),
								eq("processorName", processorName),
								eq("version", publishedVersion - 1l)
								),
						
						combine(
								set("version", publishedVersion),
								set("updatedOn", currentTimeMillis)
								),
						new UpdateOptions().upsert(false)
						);
			} catch(MongoWriteException e) {
                logger.error("Update aggregate published version has storage exception.", e);
				throw new IOExceptionOnRuntime(new IOException(e));
			} catch(RuntimeException e) {
                logger.error("Update aggregate published version has unknown exception.", e);
				throw e;
			}
		}
		
	}
	
	private long getPublishedVersion(String processorName, String aggregateRootTypeName, String aggregateRootId) {
		long r = 0;
//		{
//			DBCollection legacyCollection = mongoProvider.getLegacyCollection(getCollectionName(aggregateRootTypeName, processorName));
//			DBObject result = legacyCollection.findOne(new BasicDBObject("id", aggregateRootId));
//			if(null != result) {
//				r = (Long )result.get("version");
//			}
//		}
		{
			MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfPublishedVersionStore);
			FindIterable<Document> findIterable = collection.find(eq("aggregateRootId", aggregateRootId))
					.sort(new Document("version", -1));
			Document target = findIterable.first();
			if(null != target) {
				r = target.getLong("version");
			}
		}
		return r;
	}

}
