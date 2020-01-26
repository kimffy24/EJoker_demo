package pro.jiefzz.demo.ejoker.storage.mongo;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static pro.jiefzz.ejoker.common.system.extension.LangUtil.await;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.BulkWriteOptions;

import co.paralleluniverse.fibers.Suspendable;
import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.common.system.extension.acrossSupport.EJokerFutureTaskUtil;
import pro.jiefzz.ejoker.common.system.task.AsyncTaskResult;
import pro.jiefzz.ejoker.common.system.task.io.IOHelper;
import pro.jiefzz.ejoker.eventing.DomainEventStream;
import pro.jiefzz.ejoker.eventing.EventAppendResult;
import pro.jiefzz.ejoker.eventing.IDomainEvent;
import pro.jiefzz.ejoker.eventing.IEventSerializer;
import pro.jiefzz.ejoker.eventing.IEventStore;

@EService
public class MongoEventStore implements IEventStore {
	
	private final static Logger logger = LoggerFactory.getLogger(MongoEventStore.class);
	
	@Dependence
	MongoProvider mongoProvider;
	
	@Dependence
	IEventSerializer eventSerializer;
	
	@Dependence
	IOHelper ioHelper;
	
	/**
	 * db.EventStream.createIndex({"aggregateRootId": 1, "version": 1}, {backgroud: true, unique:true})
	 * db.EventStream.getIndexes()
	 */
	private final String versionIndexName = "aggregateRootId_1_version_1";
	
	/**
	 * db.EventStream.createIndex({"aggregateRootId": 1, "commandId": 1}, {backgroud: true, unique:true})
	 * db.EventStream.getIndexes()
	 */
	private final String commandIndexName = "aggregateRootId_1_commandId_1";
	
	private final String collectionNameOfEventStream = "EventStream";

	@Suspendable
	@Override
	public Future<AsyncTaskResult<EventAppendResult>> batchAppendAsync(
			List<DomainEventStream> eventStreams) {
		return EJokerFutureTaskUtil.completeTask(
				await(
						mongoProvider.submitWithInnerExector(
								() -> batchAppend(eventStreams)
								)
						)
				);
	}

	@Suspendable
	@Override
	public Future<AsyncTaskResult<DomainEventStream>> findAsync(String aggregateRootId, long version) {
		return EJokerFutureTaskUtil.completeTask(
				await(
						mongoProvider.submitWithInnerExector(
								() -> find(aggregateRootId, version)
								)
						)
				);
	}

	@Suspendable
	@Override
	public Future<AsyncTaskResult<DomainEventStream>> findAsync(String aggregateRootId, String commandId) {
		return EJokerFutureTaskUtil.completeTask(
				await(
						mongoProvider.submitWithInnerExector(
								() -> find(aggregateRootId, commandId)
								)
						)
				);
	}

	@Suspendable
	@Override
	public Future<AsyncTaskResult<List<DomainEventStream>>> queryAggregateEventsAsync(
			String aggregateRootId, String aggregateRootTypeName, long minVersion, long maxVersion) {
		return EJokerFutureTaskUtil.completeTask(
				await(
						mongoProvider.submitWithInnerExector(
								() -> queryAggregateEvents(aggregateRootId, aggregateRootTypeName, minVersion, maxVersion)
								)
						)
				);
	}
	
	/* private BasicDBObject legacyConvert(DomainEventStream des) {
		BasicDBObject dbObject = new BasicDBObject();

		dbObject.append("commandId", des.getCommandId());
		dbObject.append("aggregateRootTypeName", des.getAggregateRootTypeName());
		dbObject.append("aggregateRootId", des.getAggregateRootId());
		dbObject.append("version", des.getVersion());			
		dbObject.append("timestamp", des.getTimestamp());
		dbObject.append("items", des.getItems());
		
		Map<String, String> encEvents = eventSerializer.serializer(des.getEvents());
		dbObject.append("events", encEvents);
		
		return dbObject;
	}
	
	private DomainEventStream legacyRevert(BasicDBObject doc) {
		Map<String, String> encEvents = (Map<String, String> )doc.get("events");
		List<IDomainEvent<?>> srcEvents = eventSerializer.deserializer(encEvents);
		
		return new DomainEventStream(
				doc.getString("commandId"),
				doc.getString("aggregateRootId"),
				doc.getString("aggregateRootTypeName"),
				doc.getLong("version"),
				doc.getLong("timestamp"),
				srcEvents,
				(Map<String, String> )doc.get("items"));
	} */
	
	private Document convert(DomainEventStream des) {
		Document dbObject = new Document();

		dbObject.append("commandId", des.getCommandId());
		dbObject.append("aggregateRootTypeName", des.getAggregateRootTypeName());
		dbObject.append("aggregateRootId", des.getAggregateRootId());
		dbObject.append("version", des.getVersion());			
		dbObject.append("timestamp", des.getTimestamp());
		dbObject.append("items", des.getItems());
		
		Map<String, String> encEvents = eventSerializer.serializer(des.getEvents());
		dbObject.append("events", encEvents);
		
		return dbObject;
	}

	private DomainEventStream revert(Document doc) {
		Map<String, String> encEvents = (Map<String, String> )doc.get("events");
		List<IDomainEvent<?>> srcEvents = eventSerializer.deserializer(encEvents);
		
		return new DomainEventStream(
				doc.getString("commandId"),
				doc.getString("aggregateRootId"),
				doc.getString("aggregateRootTypeName"),
				doc.getLong("version"),
				srcEvents,
				(Map<String, String> )doc.get("items"));
	}
	
	private BulkWriteOptions defaultOptions = new BulkWriteOptions().ordered(false);
	
	private EventAppendResult batchAppend(List<DomainEventStream> eventStreams) {
		
		return new EventAppendResult();
//		
//		List<WriteModel<? extends Document>> requests = new ArrayList<>();
//		
//		for(DomainEventStream es : eventStreams) {
//			requests.add(new InsertOneModel<Document>(convert(es)));
//		}
//		
//		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfEventStream);
//		try {
//			collection.bulkWrite(requests, defaultOptions);
//			return EventAppendResult.Success;
//		} catch(MongoBulkWriteException e) {
//			List<BulkWriteError> writeErrors = e.getWriteErrors();
//			String message = writeErrors.iterator().next().getMessage();
//			if(null != message && message.startsWith("E11000 duplicate key")) {
//				if(message.contains(commandIndexName))
//					return EventAppendResult.DuplicateCommand;
//				else if(message.contains(versionIndexName))
//					return EventAppendResult.DuplicateEvent;
//			}
//            logger.error("Batch append event has storage exception.", e);
//			throw e;
//		} catch (RuntimeException e) {
//            logger.error("Batch append event has unknown exception.", e);
//            throw e;
//		}
	}

	private DomainEventStream find(String aggregateRootId, long version) {
//		DBCollection legacyCollection = mongoProvider.getLegacyCollection(collectionNameOfEventStream);
//		DBObject result = legacyCollection.findOne(new BasicDBObject("aggregateRootId", aggregateRootId).append("version", version));
//		if(null == result)
//			return null;
//		return legacyRevert((BasicDBObject )result);
		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfEventStream);
		FindIterable<Document> findIterable = collection.find(and(eq("aggregateRootId", aggregateRootId), eq("version", version)));
		Document target = findIterable.first();
		if(null == target)
			return null;
		return revert(target);
	}

	private DomainEventStream find(String aggregateRootId, String commandId) {
//		DBCollection legacyCollection = mongoProvider.getLegacyCollection(collectionNameOfEventStream);
//		DBObject result = legacyCollection.findOne(new BasicDBObject("aggregateRootId", aggregateRootId).append("commandId", commandId));
//		if(null == result)
//			return null;
//		return legacyRevert((BasicDBObject )result);
		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfEventStream);
		FindIterable<Document> findIterable = collection.find(and(eq("aggregateRootId", aggregateRootId), eq("commandId", commandId)));
		Document target = findIterable.first();
		if(null == target)
			return null;
		return revert(target);
	}

	private List<DomainEventStream> queryAggregateEvents(String aggregateRootId, String aggregateRootTypeName,
			long minVersion, long maxVersion) {

		List<DomainEventStream> r = new ArrayList<>();
//		{
//			DBCollection legacyCollection = mongoProvider.getLegacyCollection("EventStream");
//			DBCursor cursor = legacyCollection.find(new BasicDBObject("aggregateRootId", aggregateRootId)
//					.append("version", new Document("$gte", minVersion).append("$lt", maxVersion))
//				).sort(new BasicDBObject("version", 1));
//			List<DBObject> array = cursor.toArray();
//			for(DBObject dbo : array) {
//				BasicDBObject bdbo = (BasicDBObject )dbo;
//				if(!aggregateRootTypeName.equals(bdbo.get("aggregateRootTypeName")))
//					throw new RuntimeException("不应该啊");
//				r.add(legacyRevert(bdbo));
//			}
//		}
		{
			MongoCollection<Document> collection = mongoProvider.getCollection("EventStream");
			FindIterable<Document> findIterable = collection.find(
					and(
							eq("aggregateRootId", aggregateRootId),
							eq("aggregateRootTypeName", aggregateRootTypeName),
							and(gte("version", minVersion), lt("version", maxVersion))
							)
					);
			findIterable.sort(new Document("version", 1));
			MongoCursor<Document> mongoCursor = findIterable.iterator();
			while(mongoCursor.hasNext()) {
				r.add(revert(mongoCursor.next()));
			}
		}
		logger.error("==> aggregateRootId: {}, aggregateRootTypeName: {}, minVersion: {}, maxVersion: {}, Collection<DomainEventStream>.size(): {}",
				aggregateRootId,
				aggregateRootTypeName,
				minVersion,
				maxVersion,
				r.size()
				);
//		if(1>0)
//			throw new RuntimeException("x");
		return r;
	}
}
