package pro.jiefzz.ejoker.demo.completion.mongo.mongoSync;

import static com.jiefzz.ejoker.z.common.system.extension.LangUtil.await;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.eventing.DomainEventStream;
import com.jiefzz.ejoker.eventing.EventAppendResult;
import com.jiefzz.ejoker.eventing.IDomainEvent;
import com.jiefzz.ejoker.eventing.IEventSerializer;
import com.jiefzz.ejoker.eventing.IEventStore;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.io.IOHelper;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoWriteException;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.WriteModel;

import co.paralleluniverse.fibers.Suspendable;

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

	@Override
	public boolean isSupportBatchAppendEvent() {
		return true;
	}

	@Override
	public void setSupportBatchAppendEvent(boolean supportBatchAppendEvent) {
	}

	@Suspendable
	@Override
	public SystemFutureWrapper<AsyncTaskResult<EventAppendResult>> batchAppendAsync(
			LinkedHashSet<DomainEventStream> eventStreams) {
		return SystemFutureWrapperUtil.completeFutureTask(
				await(
						mongoProvider.submitWithInnerExector(
								() -> batchAppend(eventStreams)
								)
						)
				);
	}

	@Suspendable
	@Override
	public SystemFutureWrapper<AsyncTaskResult<EventAppendResult>> appendAsync(DomainEventStream eventStream) {
		return SystemFutureWrapperUtil.completeFutureTask(
				await(
						mongoProvider.submitWithInnerExector(
								() -> append(eventStream)
								)
						)
				);
	}

	@Suspendable
	@Override
	public SystemFutureWrapper<AsyncTaskResult<DomainEventStream>> findAsync(String aggregateRootId, long version) {
		return SystemFutureWrapperUtil.completeFutureTask(
				await(
						mongoProvider.submitWithInnerExector(
								() -> find(aggregateRootId, version)
								)
						)
				);
	}

	@Suspendable
	@Override
	public SystemFutureWrapper<AsyncTaskResult<DomainEventStream>> findAsync(String aggregateRootId, String commandId) {
		return SystemFutureWrapperUtil.completeFutureTask(
				await(
						mongoProvider.submitWithInnerExector(
								() -> find(aggregateRootId, commandId)
								)
						)
				);
	}

	@Suspendable
	@Override
	public SystemFutureWrapper<AsyncTaskResult<Collection<DomainEventStream>>> queryAggregateEventsAsync(
			String aggregateRootId, String aggregateRootTypeName, long minVersion, long maxVersion) {
		return SystemFutureWrapperUtil.completeFutureTask(
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
				doc.getLong("timestamp"),
				srcEvents,
				(Map<String, String> )doc.get("items"));
	}
	
	private BulkWriteOptions defaultOptions = new BulkWriteOptions().ordered(false);
	
	private EventAppendResult batchAppend(Set<DomainEventStream> eventStreams) {
		
		List<WriteModel<? extends Document>> requests = new ArrayList<>();
		
		for(DomainEventStream es : eventStreams) {
			requests.add(new InsertOneModel<Document>(convert(es)));
		}
		
		MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfEventStream);
		try {
			collection.bulkWrite(requests, defaultOptions);
			return EventAppendResult.Success;
		} catch(MongoBulkWriteException e) {
			List<BulkWriteError> writeErrors = e.getWriteErrors();
			String message = writeErrors.iterator().next().getMessage();
			if(null != message && message.startsWith("E11000 duplicate key")) {
				if(message.contains(commandIndexName))
					return EventAppendResult.DuplicateCommand;
				else if(message.contains(versionIndexName))
					return EventAppendResult.DuplicateEvent;
			}
            logger.error("Batch append event has storage exception.", e);
			throw e;
		} catch (RuntimeException e) {
            logger.error("Batch append event has unknown exception.", e);
            throw e;
		}
	}

	private EventAppendResult append(DomainEventStream eventStream) {
		
		if(1 > 0)
			throw new RuntimeException();
		
		{
//			BasicDBObject dbo = legacyConvert(eventStream);
//			DBCollection legacyCollection = mongoProvider.getLegacyCollection(collectionNameOfEventStream);
//			try {
//				WriteResult update = legacyCollection.update(new BasicDBObject("aggregateRootId", eventStream.getAggregateRootId())
//						.append("version", eventStream.getVersion()), dbo, true, false);
//				if(update.getN() == 1)
//					return EventAppendResult.Success;
//				else {
//					throw new RuntimeException("不被期望的意外.");
//				}
//			} catch (DuplicateKeyException ex) {
//				return EventAppendResult.DuplicateEvent;
//			}
		}
		{
			Document document = convert(eventStream);
			MongoCollection<Document> collection = mongoProvider.getCollection(collectionNameOfEventStream);
			try {
				collection.insertOne(document);
				return EventAppendResult.Success;
			} catch(MongoWriteException e) {
				String message = e.getMessage();
				if(null != message && message.startsWith("E11000 duplicate key")) {
					if(message.contains(commandIndexName))
						return EventAppendResult.DuplicateCommand;
					else if(message.contains(versionIndexName))
						return EventAppendResult.DuplicateEvent;
				}

                logger.error(String.format("Append event has storage exception, eventStream: %s", eventStream.toString()), e);
				throw e;
			} catch (RuntimeException e) {
                logger.error(String.format("Append event has unknown exception, eventStream: %s", eventStream.toString()), e);
                throw e;
			}
		}
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

	private Collection<DomainEventStream> queryAggregateEvents(String aggregateRootId, String aggregateRootTypeName,
			long minVersion, long maxVersion) {

		Collection<DomainEventStream> r = new ArrayList<>();
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
