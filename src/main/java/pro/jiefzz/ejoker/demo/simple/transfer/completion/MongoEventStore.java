package pro.jiefzz.ejoker.demo.simple.transfer.completion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

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
import com.jiefzz.ejoker.z.common.io.AsyncTaskResult;
import com.jiefzz.ejoker.z.common.io.IOHelper;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.EJokerFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.DuplicateKeyException;
import com.mongodb.WriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

//@EService
public class MongoEventStore implements IEventStore {
	
	private final static Logger logger = LoggerFactory.getLogger(MongoEventStore.class);
	
	@Dependence
	MongoProvider mongoProvider;
	
	@Dependence
	IEventSerializer eventSerializer;
	
	@Dependence
	IOHelper ioHelper;

	@Override
	public boolean isSupportBatchAppendEvent() {
		return false;
	}

	@Override
	public void setSupportBatchAppendEvent(boolean supportBatchAppendEvent) {
		;
	}

	@Override
	public SystemFutureWrapper<AsyncTaskResult<EventAppendResult>> batchAppendAsync(
			LinkedHashSet<DomainEventStream> eventStreams) {
		return null;
	}

	@Override
	public SystemFutureWrapper<AsyncTaskResult<EventAppendResult>> appendAsync(DomainEventStream eventStream) {
		return EJokerFutureWrapperUtil.createCompleteFutureTask(append(eventStream));
	}

	@Override
	public SystemFutureWrapper<AsyncTaskResult<DomainEventStream>> findAsync(String aggregateRootId, long version) {
		return EJokerFutureWrapperUtil.createCompleteFutureTask(find(aggregateRootId, version));
	}

	@Override
	public SystemFutureWrapper<AsyncTaskResult<DomainEventStream>> findAsync(String aggregateRootId, String commandId) {
		return EJokerFutureWrapperUtil.createCompleteFutureTask(find(aggregateRootId, commandId));
	}

	@Override
	public SystemFutureWrapper<AsyncTaskResult<Collection<DomainEventStream>>> queryAggregateEventsAsync(
			String aggregateRootId, String aggregateRootTypeName, long minVersion, long maxVersion) {
		Collection<DomainEventStream> queryAggregateEvents = queryAggregateEvents(aggregateRootId, aggregateRootTypeName, minVersion, maxVersion);
		return EJokerFutureWrapperUtil.createCompleteFutureTask(queryAggregateEvents);
	}
//	
//	private Document convert(DomainEventStream des) {
//		Document document = new Document();
//
//		document.append("commandId", des.getCommandId());
//		document.append("aggregateRootTypeName", des.getAggregateRootTypeName());
//		document.append("aggregateRootId", des.getAggregateRootId());
//		document.append("version", des.getVersion());			
//		document.append("timestamp", des.getTimestamp());
//		document.append("items", des.getItems());
//		
//		Map<String, String> encEvents = eventSerializer.serializer(des.getEvents());
//		document.append("events", encEvents  );
//		
//		return document;
//	}
//
//	private DomainEventStream revert(Document doc) {
//		Map<String, String> encEvents = (Map<String, String> )doc.get("events");
//		List<IDomainEvent<?>> srcEvents = eventSerializer.deserializer(encEvents);
//		
//		return new DomainEventStream(
//				doc.getString("commandId"),
//				doc.getString("aggregateRootId"),
//				doc.getString("aggregateRootTypeName"),
//				doc.getLong("version"),
//				doc.getLong("timestamp"),
//				srcEvents,
//				(Map<String, String> )doc.get("items"));
//	}
	
	private BasicDBObject legacyConvert(DomainEventStream des) {
		BasicDBObject dbObject = new BasicDBObject();

		dbObject.append("commandId", des.getCommandId());
		dbObject.append("aggregateRootTypeName", des.getAggregateRootTypeName());
		dbObject.append("aggregateRootId", des.getAggregateRootId());
		dbObject.append("version", des.getVersion());			
		dbObject.append("timestamp", des.getTimestamp());
		dbObject.append("items", des.getItems());
		
		Map<String, String> encEvents = eventSerializer.serializer(des.getEvents());
		dbObject.append("events", encEvents  );
		
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
	}

	@Override
	public EventAppendResult batchAppend(LinkedHashSet<DomainEventStream> eventStreams) {
		return null;
	}

	@Override
	public EventAppendResult append(DomainEventStream eventStream) {
		
		{
			BasicDBObject dbo = legacyConvert(eventStream);
			DBCollection legacyCollection = mongoProvider.getLegacyCollection("EventStream");
			try {
				WriteResult update = legacyCollection.update(new BasicDBObject("aggregateRootId", eventStream.getAggregateRootId())
						.append("version", eventStream.getVersion()), dbo, true, false);
				if(update.getN() == 1)
					return EventAppendResult.Success;
				else {
					throw new RuntimeException("不被期望的意外.");
				}
			} catch (DuplicateKeyException ex) {
				return EventAppendResult.DuplicateEvent;
			}
			
		}
//
//		Document c = convert(eventStream);
//		MongoCollection<Document> collection = mongoProvider.getCollection("EventStream");
//		collection.insertOne(c);
//		
//		return EventAppendResult.Success;
	}

	@Override
	public DomainEventStream find(String aggregateRootId, long version) {
		DBCollection legacyCollection = mongoProvider.getLegacyCollection("EventStream");
		DBObject result = legacyCollection.findOne(new BasicDBObject("aggregateRootId", aggregateRootId).append("version", version));
		if(null == result)
			return null;
		return legacyRevert((BasicDBObject )result);
//		MongoCollection<Document> collection = mongoProvider.getCollection("EventStream");
//		FindIterable<Document> find = collection.find(new Document("aggregateRootId", aggregateRootId).append("version", version));
//		Document target = find.first();
//		return revert(target);
	}

	@Override
	public DomainEventStream find(String aggregateRootId, String commandId) {
		DBCollection legacyCollection = mongoProvider.getLegacyCollection("EventStream");
		DBObject result = legacyCollection.findOne(new BasicDBObject("aggregateRootId", aggregateRootId).append("commandId", commandId));
		if(null == result)
			return null;
		return legacyRevert((BasicDBObject )result);
//		MongoCollection<Document> collection = mongoProvider.getCollection("EventStream");
//		FindIterable<Document> find = collection.find(new Document("aggregateRootId", aggregateRootId).append("commandId", commandId));
//		Document target = find.first();
//		DomainEventStream des = null;
//		if(null != target)
//			des = revert(target);
//		return des;
	}

	@Override
	public Collection<DomainEventStream> queryAggregateEvents(String aggregateRootId, String aggregateRootTypeName,
			long minVersion, long maxVersion) {

		Collection<DomainEventStream> r = new ArrayList<>();
//		{
//			MongoCollection<Document> collection = mongoProvider.getCollection("EventStream");
//			FindIterable<Document> find = collection.find(
//					new Document("aggregateRootId", aggregateRootId)
//						.append("aggregateRootTypeName", aggregateRootTypeName)
//						.append("version", new Document("$gte", minVersion).append("$lt", maxVersion))
//					).sort(new Document("version", 1));
//			MongoCursor<Document> mongoCursor = find.iterator();
//			while(mongoCursor.hasNext()) {
//				r.add(revert(mongoCursor.next()));
//			}
//		}
		{
			DBCollection legacyCollection = mongoProvider.getLegacyCollection("EventStream");
			DBCursor cursor = legacyCollection.find(new BasicDBObject("aggregateRootId", aggregateRootId)
					.append("version", new Document("$gte", minVersion).append("$lt", maxVersion))
				).sort(new BasicDBObject("version", 1));
			List<DBObject> array = cursor.toArray();
			for(DBObject dbo : array) {
				BasicDBObject bdbo = (BasicDBObject )dbo;
				if(!aggregateRootTypeName.equals(bdbo.get("aggregateRootTypeName")))
					throw new RuntimeException("不应该啊");
				r.add(legacyRevert(bdbo));
			}
		}
		logger.error("==> aggregateRootId: {}, aggregateRootTypeName: {}, minVersion: {}, maxVersion: {}, Collection<DomainEventStream>.size(): {}",
				aggregateRootId,
				aggregateRootTypeName,
				minVersion,
				maxVersion,
				r.size()
				);
		return r;
	}
}
