package pro.jiefzz.ejoker.demo.completion.mongo.mongoAsync;
//package pro.jiefzz.ejoker.demo.completion;
//
//import static com.jiefzz.ejoker.z.common.system.extension.LangUtil.await;
//import static com.mongodb.client.model.Filters.and;
//import static com.mongodb.client.model.Filters.eq;
//import static com.mongodb.client.model.Filters.gte;
//import static com.mongodb.client.model.Filters.lt;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Future;
//
//import org.bson.Document;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.jiefzz.ejoker.eventing.DomainEventStream;
//import com.jiefzz.ejoker.eventing.EventAppendResult;
//import com.jiefzz.ejoker.eventing.IDomainEvent;
//import com.jiefzz.ejoker.eventing.IEventSerializer;
//import com.jiefzz.ejoker.eventing.IEventStore;
//import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
//import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
//import com.jiefzz.ejoker.z.common.io.IOHelper;
//import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.RipenFuture;
//import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
//import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
//import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;
//import com.mongodb.MongoWriteException;
//import com.mongodb.async.client.FindIterable;
//import com.mongodb.async.client.MongoCollection;
//
//import co.paralleluniverse.fibers.Suspendable;
//
//@EService
//public class MongoAsyncEventStore implements IEventStore {
//
//	private final static Logger logger = LoggerFactory.getLogger(MongoAsyncEventStore.class);
//
//	@Dependence
//	MongoAsyncProvider mongoProvider;
//
//	@Dependence
//	IEventSerializer eventSerializer;
//
//	@Dependence
//	IOHelper ioHelper;
//
//	/**
//	 * db.EventStream.createIndex({"aggregateRootId": 1, "version": 1}, {backgroud:
//	 * true, unique:true}) db.EventStream.getIndexes()
//	 */
//	private final String versionIndexName = "aggregateRootId_1_version_1";
//
//	/**
//	 * db.EventStream.createIndex({"aggregateRootId": 1, "commandId": 1},
//	 * {backgroud: true, unique:true}) db.EventStream.getIndexes()
//	 */
//	private final String commandIndexName = "aggregateRootId_1_commandId_1";
//
//	private final String collectionNameOfEventStream = "EventStream";
//
//	@Override
//	public boolean isSupportBatchAppendEvent() {
//		return false;
//	}
//
//	@Override
//	public void setSupportBatchAppendEvent(boolean supportBatchAppendEvent) {
//		;
//	}
//
//	@Suspendable
//	@Override
//	public SystemFutureWrapper<AsyncTaskResult<EventAppendResult>> batchAppendAsync(
//			LinkedHashSet<DomainEventStream> eventStreams) {
//		return null;
//	}
//
//	@Suspendable
//	@Override
//	public SystemFutureWrapper<AsyncTaskResult<EventAppendResult>> appendAsync(DomainEventStream eventStream) {
//		return SystemFutureWrapperUtil.createCompleteFutureTask(append(eventStream));
//	}
//
//	@Suspendable
//	@Override
//	public SystemFutureWrapper<AsyncTaskResult<DomainEventStream>> findAsync(String aggregateRootId, long version) {
//		return SystemFutureWrapperUtil.createCompleteFutureTask(find(aggregateRootId, version));
//	}
//
//	@Suspendable
//	@Override
//	public SystemFutureWrapper<AsyncTaskResult<DomainEventStream>> findAsync(String aggregateRootId, String commandId) {
//		return SystemFutureWrapperUtil.createCompleteFutureTask(find(aggregateRootId, commandId));
//	}
//
//	@Suspendable
//	@Override
//	public SystemFutureWrapper<AsyncTaskResult<Collection<DomainEventStream>>> queryAggregateEventsAsync(
//			String aggregateRootId, String aggregateRootTypeName, long minVersion, long maxVersion) {
//		Collection<DomainEventStream> queryAggregateEvents = queryAggregateEvents(aggregateRootId,
//				aggregateRootTypeName, minVersion, maxVersion);
//		return SystemFutureWrapperUtil.createCompleteFutureTask(queryAggregateEvents);
//	}
//
//	@Suspendable
//	private Document convert(DomainEventStream des) {
//		Document dbObject = new Document();
//
//		dbObject.append("commandId", des.getCommandId());
//		dbObject.append("aggregateRootTypeName", des.getAggregateRootTypeName());
//		dbObject.append("aggregateRootId", des.getAggregateRootId());
//		dbObject.append("version", des.getVersion());
//		dbObject.append("timestamp", des.getTimestamp());
//		dbObject.append("items", des.getItems());
//
//		Map<String, String> encEvents = eventSerializer.serializer(des.getEvents());
//		dbObject.append("events", encEvents);
//
//		return dbObject;
//	}
//
//	@Suspendable
//	private DomainEventStream revert(Document doc) {
//		Map<String, String> encEvents = (Map<String, String>) doc.get("events");
//		List<IDomainEvent<?>> srcEvents = eventSerializer.deserializer(encEvents);
//
//		return new DomainEventStream(doc.getString("commandId"), doc.getString("aggregateRootId"),
//				doc.getString("aggregateRootTypeName"), doc.getLong("version"), doc.getLong("timestamp"), srcEvents,
//				(Map<String, String>) doc.get("items"));
//	}
//
//	@Suspendable
//	private EventAppendResult batchAppend(LinkedHashSet<DomainEventStream> eventStreams) {
//		throw new RuntimeException("Unsupport!!!");
//	}
//
//	@Suspendable
//	private EventAppendResult append(DomainEventStream eventStream) {
//
//		Document document = convert(eventStream);
//		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfEventStream);
//
//		RipenFuture<EventAppendResult> ripenFuture = new RipenFuture<>();
//		collection.insertOne(document, (r, e) -> {
//			if (null != e) {
//				if (e instanceof MongoWriteException) {
//					String message = e.getMessage();
//					if (null != message && message.startsWith("E11000 duplicate key")) {
//						if (message.contains(commandIndexName)) {
//							ripenFuture.trySetResult(EventAppendResult.DuplicateCommand);
//							return;
//						} else if (message.contains(versionIndexName)) {
//							ripenFuture.trySetResult(EventAppendResult.DuplicateEvent);
//							return;
//						}
//					}
//				}
//				logger.error(
//						String.format("Append event has storage exception, eventStream: %s", eventStream.toString()),
//						e);
//				ripenFuture.trySetException(e);
//			} else {
//				ripenFuture.trySetResult(EventAppendResult.Success);
//			}
//		});
//		return await(ripenFuture);
//	}
//
//	@Suspendable
//	private DomainEventStream find(String aggregateRootId, long version) {
//		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfEventStream);
//		FindIterable<Document> findIterable = collection
//				.find(and(eq("aggregateRootId", aggregateRootId), eq("version", version)));
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
//		
//		if (null == target)
//			return null;
//		return revert(target);
//	}
//
//	@Suspendable
//	private DomainEventStream find(String aggregateRootId, String commandId) {
//		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfEventStream);
//		FindIterable<Document> findIterable = collection
//				.find(and(eq("aggregateRootId", aggregateRootId), eq("commandId", commandId)));
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
//		
//		if (null == target)
//			return null;
//		return revert(target);
//	}
//
//	@Suspendable
//	private Collection<DomainEventStream> queryAggregateEvents(String aggregateRootId, String aggregateRootTypeName,
//			long minVersion, long maxVersion) {
//
//		Collection<DomainEventStream> r = new ArrayList<>();
//		MongoCollection<Document> collection = mongoProvider.getCollection("EventStream");
//		FindIterable<Document> findIterable = collection
//				.find(and(eq("aggregateRootId", aggregateRootId), eq("aggregateRootTypeName", aggregateRootTypeName),
//						and(gte("version", minVersion), lt("version", maxVersion))))
//				.sort(new Document("version", 1));
//
//		RipenFuture<Void> ripenFuture = new RipenFuture<>();
//		findIterable.forEach(d -> r.add(revert(d)), (n, e) -> {
//			if(null != e) {
//				ripenFuture.trySetException(e);
//			} else {
//				ripenFuture.trySetResult(null);
//			}
//		});
//		await(ripenFuture);
//		
//		logger.debug(
//				"==> aggregateRootId: {}, aggregateRootTypeName: {}, minVersion: {}, maxVersion: {}, Collection<DomainEventStream>.size(): {}",
//				aggregateRootId, aggregateRootTypeName, minVersion, maxVersion, r.size());
//		
//		return r;
//	}
//}
