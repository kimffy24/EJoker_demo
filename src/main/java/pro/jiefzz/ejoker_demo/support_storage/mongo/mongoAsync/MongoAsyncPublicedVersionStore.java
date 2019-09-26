package pro.jiefzz.ejoker_demo.support_storage.mongo.mongoAsync;
//package pro.jiefzz.ejoker.demo.completion;
//
//import static com.jiefzz.ejoker.z.common.system.extension.LangUtil.await;
//import static com.mongodb.client.model.Filters.and;
//import static com.mongodb.client.model.Filters.eq;
//import static com.mongodb.client.model.Updates.combine;
//import static com.mongodb.client.model.Updates.set;
//
//import java.io.IOException;
//
//import org.bson.Document;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.jiefzz.ejoker.infrastructure.IPublishedVersionStore;
//import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
//import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
//import com.jiefzz.ejoker.z.common.io.IOExceptionOnRuntime;
//import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.RipenFuture;
//import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
//import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
//import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;
//import com.jiefzz.ejoker.z.common.task.AsyncTaskStatus;
//import com.mongodb.MongoWriteException;
//import com.mongodb.async.client.FindIterable;
//import com.mongodb.async.client.MongoCollection;
//import com.mongodb.client.model.UpdateOptions;
//
//import co.paralleluniverse.fibers.Suspendable;
//
//@EService
//public class MongoAsyncPublicedVersionStore implements IPublishedVersionStore {
//
//	private final static Logger logger = LoggerFactory.getLogger(MongoAsyncPublicedVersionStore.class);
//
//	@Dependence
//	MongoAsyncProvider mongoProvider;
//
//	/**
//	 * db.PublicedVersionStore.createIndex({"aggregateRootId":1, "processorName":1,
//	 * "version":1}, {backgroud: true, unique:true})
//	 * db.PublicedVersionStore.getIndexes()
//	 */
//	private final String aggregateVersionIndexName = "aggregateRootId_1_processorName_1_version_1";
//
//	private final String collectionNameOfPublishedVersionStore = "PublicedVersionStore";
//
//	@Suspendable
//	@Override
//	public SystemFutureWrapper<AsyncTaskResult<Void>> updatePublishedVersionAsync(String processorName,
//			String aggregateRootTypeName, String aggregateRootId, long publishedVersion) {
//		try {
//			updatePublishedVersion(processorName, aggregateRootTypeName, aggregateRootId, publishedVersion);
//			return SystemFutureWrapperUtil.createCompleteFutureTask();
//		} catch (Exception e) {
//			RipenFuture<AsyncTaskResult<Void>> ripenFuture = new RipenFuture<>();
//			ripenFuture.trySetResult(new AsyncTaskResult<>(AsyncTaskStatus.Failed, e.getMessage(), null));
//			return new SystemFutureWrapper<>(ripenFuture);
//		}
//	}
//
//	@Suspendable
//	@Override
//	public SystemFutureWrapper<AsyncTaskResult<Long>> getPublishedVersionAsync(String processorName,
//			String aggregateRootTypeName, String aggregateRootId) {
//		try {
//			long r = getPublishedVersion(processorName, aggregateRootTypeName, aggregateRootId);
//			return SystemFutureWrapperUtil.createCompleteFutureTask(r);
//		} catch (Exception e) {
//			RipenFuture<AsyncTaskResult<Long>> ripenFuture = new RipenFuture<>();
//			ripenFuture.trySetResult(new AsyncTaskResult<>(AsyncTaskStatus.Failed, e.getMessage(), null));
//			return new SystemFutureWrapper<>(ripenFuture);
//		}
//	}
//
//	@Suspendable
//	private void updatePublishedVersion(String processorName, String aggregateRootTypeName, String aggregateRootId,
//			long publishedVersion) {
//
//		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfPublishedVersionStore);
//		RipenFuture<Document> ripenFuture = new RipenFuture<>();
//		long currentTimeMillis = System.currentTimeMillis();
//		if (0 == 1 - publishedVersion) {
//			collection.insertOne(new Document("aggregateRootId", aggregateRootId).append("version", 1l)
//					.append("processorName", processorName).append("aggregateRootTypeName", aggregateRootTypeName)
//					.append("createdOn", currentTimeMillis).append("updatedOn", currentTimeMillis), (r, e) -> {
//						if (null != e) {
//							if (e instanceof MongoWriteException) {
//								String message = e.getMessage();
//								if (null != message && message.startsWith("E11000 duplicate key")) {
//									if (message.contains(aggregateVersionIndexName)) {
//										ripenFuture.trySetResult(null);
//										return;
//									}
//								}
//								logger.error("Update aggregate published version has storage exception.", e);
//								ripenFuture.trySetException(new IOExceptionOnRuntime(new IOException(e)));
//								return;
//							} else {
//								logger.error("Update aggregate published version has unknown exception.", e);
//								ripenFuture.trySetException(e);
//								return;
//							}
//						}
//						ripenFuture.trySetResult(null);
//					});
//		} else {
//			collection.updateOne(
//					and(eq("aggregateRootId", aggregateRootId), eq("processorName", processorName),
//							eq("version", publishedVersion - 1l)),
//					combine(set("version", publishedVersion), set("updatedOn", currentTimeMillis)),
//					new UpdateOptions().upsert(false), (r, e) -> {
//						if (null != e) {
//							if (e instanceof MongoWriteException) {
//								logger.error("Update aggregate published version has storage exception.", e);
//								ripenFuture.trySetException(new IOExceptionOnRuntime(new IOException(e)));
//								return;
//							} else {
//								logger.error("Update aggregate published version has unknown exception.", e);
//								ripenFuture.trySetException(e);
//								return;
//							}
//						}
//						ripenFuture.trySetResult(null);
//					});
//		}
//
//	}
//
//	@Suspendable
//	private long getPublishedVersion(String processorName, String aggregateRootTypeName, String aggregateRootId) {
//		long r = 0;
//		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfPublishedVersionStore);
//		FindIterable<Document> findIterable = collection.find(eq("aggregateRootId", aggregateRootId))
//				.sort(new Document("version", -1));
//
//		RipenFuture<Document> ripenFuture = new RipenFuture<>();
//		findIterable.first((d, e) -> {
//			if (null != e) {
//				ripenFuture.trySetException(e);
//			} else {
//				ripenFuture.trySetResult(d);
//			}
//		});
//		Document target = await(ripenFuture);
//		if (null != target) {
//			r = target.getLong("version");
//		}
//		return r;
//
//	}
//
//}
