package pro.jiefzz.ejoker_demo.transfer.debug;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.ejoker.commanding.ICommandProcessor;
import pro.jiefzz.ejoker.commanding.IProcessingCommandHandler;
import pro.jiefzz.ejoker.commanding.ProcessingCommandMailbox;
import pro.jiefzz.ejoker.eventing.EventCommittingContextMailBox;
import pro.jiefzz.ejoker.eventing.IEventCommittingService;
import pro.jiefzz.ejoker.eventing.IEventStore;
import pro.jiefzz.ejoker.eventing.impl.InMemoryEventStore;
import pro.jiefzz.ejoker.eventing.impl.InMemoryEventStore.AggregateInfo;
import pro.jiefzz.ejoker.eventing.qeventing.IProcessingEventProcessor;
import pro.jiefzz.ejoker.eventing.qeventing.IPublishedVersionStore;
import pro.jiefzz.ejoker.eventing.qeventing.ProcessingEventMailBox;
import pro.jiefzz.ejoker.eventing.qeventing.impl.InMemoryPublishedVersionStore;
import pro.jiefzz.ejoker.messaging.IMessageDispatcher;
import pro.jiefzz.ejoker.queue.SendQueueMessageService;
import pro.jiefzz.ejoker.z.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.z.context.annotation.context.EInitialize;
import pro.jiefzz.ejoker.z.context.annotation.context.ESType;
import pro.jiefzz.ejoker.z.context.annotation.context.EService;
import pro.jiefzz.ejoker.z.system.extension.acrossSupport.EJokerFutureTaskUtil;
import pro.jiefzz.ejoker.z.system.helper.MapHelper;
import pro.jiefzz.ejoker.z.system.task.AsyncTaskResult;

@EService(type = ESType.MESSAGE_HANDLER)
public class DebugHelperEJoker extends DAssemblier {
	
	private final static Logger logger = LoggerFactory.getLogger(DebugHelperEJoker.class);
	
	@Dependence
	IPublishedVersionStore inMemoryPublishedVersionStore;
	
	@Dependence
	IEventStore eventStore;
	
	@Dependence
	ICommandProcessor commandProcessor;

	@Dependence
	IProcessingCommandHandler processingCommandHandler;
	
	@Dependence
	IEventCommittingService eventService;
	
	@Dependence
	IProcessingEventProcessor processingEventProcessor;
	
	@Dependence
	IMessageDispatcher messageDispatcher;

	@Dependence
	SendQueueMessageService sendQueueMessageService;
	
	// ============ 
	
	Map<String, ProcessingCommandMailbox> commandMailboxDict = null;
	
	List<EventCommittingContextMailBox> eventCommittingContextMailBoxList = null;
	
	Map<String, ProcessingEventMailBox> eventMailboxDict = null;
	
	Map<String, AggregateInfo> mStorage = null;
	
	Map<String, Long> versionDict = null;
	
//	
//	/**
//	 * 列出某个聚合的领域基础设施中的所有状态和属性
//	 * @param aggregateRootId 目标的聚合根ID
//	 */
//	private void profileAggregateProperties(final String aggregateRootId) {
//
//		// 提取并输出CommandMailbox的状态和属性
//		Optional<Entry<String, ProcessingCommandMailbox>> findFirst = getCommandMailBoxDict().entrySet()
//				.parallelStream().filter(e -> aggregateRootId.equals(e.getKey())).findFirst();
//		if (findFirst.isPresent()) {
//			Entry<String, ProcessingCommandMailbox> processingCommandMailboxEntry = findFirst.get();
//			ProcessingCommandMailbox box = processingCommandMailboxEntry.getValue();
//			long consumingSequence;
//			long consumedSequence;
//			Map<Long, ProcessingCommand> messageDict;
//			Map<Long, CommandResult> requestToCompleteCommandDict;
//			Object enqueueLock;
//			Object asyncLock;
//			long nextSequence;
//			long lastActiveTime;
//			AtomicBoolean onRunning;
//			AtomicBoolean onPaused;
//			AtomicBoolean onProcessing;
//			try {
//				messageDict = (Map<Long, ProcessingCommand>) dF_AbstractAggregateMessageMailBox_messageDict.get(box);
//				requestToCompleteCommandDict = (Map<Long, CommandResult>) dF_AbstractAggregateMessageMailBox_requestToCompleteMessageDict.get(box);
//				enqueueLock = dF_AbstractAggregateMessageMailBox_enqueueLock.get(box);
//				asyncLock = dF_AbstractAggregateMessageMailBox_asyncLock.get(box);
//				nextSequence = (long) dF_AbstractAggregateMessageMailBox_nextSequence.get(box);
//				consumingSequence = (long) dF_AbstractAggregateMessageMailBox_consumingSequence.get(box);
//				consumedSequence = (long) dF_AbstractAggregateMessageMailBox_consumedSequence.get(box);
//
//				onRunning = (AtomicBoolean) dF_AbstractAggregateMessageMailBox_onRunning.get(box);
//				onPaused = (AtomicBoolean) dF_AbstractAggregateMessageMailBox_onPaused.get(box);
//				onProcessing = (AtomicBoolean) dF_AbstractAggregateMessageMailBox_onProcessing.get(box);
//
//				lastActiveTime = (long) dF_AbstractAggregateMessageMailBox_lastActiveTime.get(box);
//			} catch (IllegalArgumentException | IllegalAccessException e1) {
//				logger.error("ProcessingCommandMailbox[aggregateRootId={}] cannot be profile!!!", aggregateRootId);
//				logger.error("", e1);
//				throw new RuntimeException(e1);
//			}
//
//			Set<Long> keySet = messageDict.keySet();
//			Set<Long> keySet2 = requestToCompleteCommandDict.keySet();
//			logger.error(
//					"processingCommandMailbox: \n\tenqueueLock: {}, " + "\n\tasyncLock: {},"
//							+ "\n\tmessageDict.keys: {}," + "\n\trequestToCompleteCommandDict.keys: {},"
//							+ "\n\tnextSequence: {}," + "\n\tconsumingSequence: {}," + "\n\tconsumedSequence: {},"
//							+ "\n\tonRunning.get(): {}," + "\n\tonPaused.get(): {}," + "\n\tonProcessing.get(): {},"
//							+ "\n\tlastActiveTime: {}",
//					enqueueLock, asyncLock, keySet.toString(), keySet2.toString(), nextSequence, consumingSequence,
//					consumedSequence, box.isRunning(), onPaused.get(), onProcessing.get(), lastActiveTime);
//		}
//		
////		// 提取并输出EventMailbox的状态和属性
////		Optional<Entry<String, EventMailBox>> findEventMailBox = getEventMailBoxDict().entrySet()
////				.parallelStream().filter(e -> aggregateRootId.equals(e.getKey())).findFirst();
////		if (findEventMailBox.isPresent()) {
////			Entry<String, EventMailBox> EventMailBoxEntry = findEventMailBox.get();
////			EventMailBox box = EventMailBoxEntry.getValue();
////			long consumingSequence;
////			long consumedSequence;
////			Map<Long, EventCommittingContext> messageDict;
////			Map<Long, Void> requestToCompleteCommandDict;
////			Object enqueueLock;
////			Object asyncLock;
////			long nextSequence;
////			long lastActiveTime;
////			AtomicBoolean onRunning;
////			AtomicBoolean onPaused;
////			AtomicBoolean onProcessing;
////			try {
////				messageDict = (Map<Long, EventCommittingContext>) dF_AbstractAggregateMessageMailBox_messageDict.get(box);
////				requestToCompleteCommandDict = (Map<Long, Void>) dF_AbstractAggregateMessageMailBox_requestToCompleteMessageDict.get(box);
////				enqueueLock = dF_AbstractAggregateMessageMailBox_enqueueLock.get(box);
////				asyncLock = dF_AbstractAggregateMessageMailBox_asyncLock.get(box);
////				nextSequence = (long) dF_AbstractAggregateMessageMailBox_nextSequence.get(box);
////				consumingSequence = (long) dF_AbstractAggregateMessageMailBox_consumingSequence.get(box);
////				consumedSequence = (long) dF_AbstractAggregateMessageMailBox_consumedSequence.get(box);
////
////				onRunning = (AtomicBoolean) dF_AbstractAggregateMessageMailBox_onRunning.get(box);
////				onPaused = (AtomicBoolean) dF_AbstractAggregateMessageMailBox_onPaused.get(box);
////				onProcessing = (AtomicBoolean) dF_AbstractAggregateMessageMailBox_onProcessing.get(box);
////
////				lastActiveTime = (long) dF_AbstractAggregateMessageMailBox_lastActiveTime.get(box);
////			} catch (IllegalArgumentException | IllegalAccessException e1) {
////				logger.error("EventMailBox[aggregateRootId={}] cannot be profile!!!", aggregateRootId);
////				logger.error("", e1);
////				throw new RuntimeException(e1);
////			}
////
////			Set<Long> keySet = messageDict.keySet();
////			Set<Long> keySet2 = requestToCompleteCommandDict.keySet();
////			logger.error(
////					"eventMailbox: \n\tenqueueLock: {}, " + "\n\tasyncLock: {},"
////							+ "\n\tmessageDict.keys: {}," + "\n\trequestToCompleteCommandDict.keys: {},"
////							+ "\n\tnextSequence: {}," + "\n\tconsumingSequence: {}," + "\n\tconsumedSequence: {},"
////							+ "\n\tonRunning.get(): {}," + "\n\tonPaused.get(): {}," + "\n\tonProcessing.get(): {},"
////							+ "\n\tlastActiveTime: {}",
////					enqueueLock, asyncLock, keySet.toString(), keySet2.toString(), nextSequence, consumingSequence,
////					consumedSequence, box.isRunning(), onPaused.get(), onProcessing.get(), lastActiveTime);
////		}
//
//		// 提取并输出Q端MessageMailbox的状态和属性
//		Optional<Entry<String, ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>>> findProcessingMessageMailbox = getProcessingMessageMailBoxDict()
//				.entrySet().parallelStream().filter(e -> aggregateRootId.equals(e.getKey())).findFirst();
//		if (findProcessingMessageMailbox.isPresent()) {
//			ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage> value = (ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>) findProcessingMessageMailbox
//					.get();
//			Set<Long> keySet;
//			Map<Long, ProcessingDomainEventStreamMessage> map = getWaitingMessageDict(value);
//			String routingKey = getProcessingMessageMailboxRroutingKey(value);
//			Queue<ProcessingDomainEventStreamMessage> messageQueue = getMessageQueue(value);
//			if (null != map)
//				keySet = map.keySet();
//			else
//				keySet = new HashSet<>();
//
//			long lastActiveTime;
//			try {
//				lastActiveTime = (long) dF_ProcessingMessageMailbox_lastActiveTime.get(value);
//			} catch (IllegalArgumentException | IllegalAccessException e1) {
//				logger.error("ProcessingCommandMailbox[aggregateRootId={}] cannot be probe!!!", aggregateRootId);
//				logger.error("", e1);
//				throw new RuntimeException(e1);
//			}
//
//			logger.error(
//					"processingMessageMailBox: \n\troutingKey: {}, " + "\n\tmessageQueue.size():, {}"
//							+ "\n\twaitingMessageDict: {}, " + "\n\tonRunning.get(): {}, " + "\n\tlastActiveTime: {}",
//					routingKey, messageQueue.size(), keySet.toString(), value.onRunning(), lastActiveTime);
//		}
//		
//		// 输出内存中记录的版本号(如果使用内存版本库)
//		if("InMemoryPublishedVersionStore".equals(inMemoryPublishedVersionStore.getClass().getSimpleName()))
//			logger.error("inMemoryPublishedVersionStore: \n\tversionDict: {}", versionDict.get("defaultEventProcessor-" + aggregateRootId));
//		
//		// 输出内存事件库中的事件(如果使用内存事件库)
//		if("InMemoryEventStore".equals(eventStore.getClass().getSimpleName())) {
//			Map<String, DomainEventStream> map = mStorage.get(aggregateRootId);
//			if(null != map)
//				logger.error("{}", map.toString());
//		}
//	}
//	
//	/**
//	 * 获取Q端事件流信箱的routingKey
//	 * @param target
//	 * @return
//	 */
//	private String getProcessingMessageMailboxRroutingKey(ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> target) {
//		try {
//			return (String )dF_ProcessingMessageMailbox_routingKey.get(target);
//		} catch (IllegalArgumentException | IllegalAccessException e) {
//			e.printStackTrace();
//			return "";
//		}
//	}
//	
//	/**
//	 * 获得Q端事件流信箱的待处理队列
//	 * @param target
//	 * @return
//	 */
//	private Queue<ProcessingDomainEventStreamMessage> getMessageQueue(ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> target) {
//		try {
//			return (Queue<ProcessingDomainEventStreamMessage> )dF_ProcessingMessageMailbox_messageQueue.get(target);
//		} catch (IllegalArgumentException | IllegalAccessException e) {
//			e.printStackTrace();
//			return new LinkedBlockingQueue<>();
//		}
//	}
//
//	/**
//	 * 获取Q端事件流信箱的延后处理集合
//	 * @param target
//	 * @return
//	 */
//	private Map<Long, ProcessingDomainEventStreamMessage> getWaitingMessageDict(ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> target) {
//		try {
//			return (Map<Long, ProcessingDomainEventStreamMessage> )dF_ProcessingMessageMailbox_waitingMessageDict.get(target);
//		} catch (IllegalArgumentException | IllegalAccessException e) {
//			e.printStackTrace();
//			return new HashMap<>();
//		}
//	}
	
	/**
	 * 无
	 * @param dm
	 * @return
	 */
	public Future<AsyncTaskResult<Void>> handleAsync(DebugMessage dm) {
		logger.debug("Receive a message: {}", dm.getClass().getSimpleName());
		return EJokerFutureTaskUtil.completeTask();
	}

//	/**
//	 * 列出某个聚合的领域基础设施中的所有状态和属性
//	 * @param dm
//	 * @return
//	 */
//	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage2 dm) {
//
//		profileAggregateProperties(dm.aId);
//		
//		return SystemFutureWrapperUtil.completeFutureTask();
//	}
//
//	/**
//	 * 对dm3类型消息的响应
//	 * * 调度每一个mailbox
//	 * @param dm
//	 * @return
//	 */
//	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage3 dm) {
//		
//		getProcessingMessageMailBoxDict().entrySet().parallelStream().forEach(e -> {
//			ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> value = e.getValue();
//			processingMessageScheduler.scheduleMailbox(value);
//		});
//		
////		getEventMailBoxDict().entrySet().parallelStream().forEach(e -> {
////			EventMailBox value = e.getValue();
////			value.tryRun();
////		});
//		
//		getCommandMailBoxDict().entrySet().parallelStream().forEach(e -> {
//			ProcessingCommandMailbox value = e.getValue();
//			value.tryRun();
//		});
//		
//		logger.error("===================================================================================================== ok !!!!");
//		
//		return SystemFutureWrapperUtil.completeFutureTask();
//	}
	
	public void probe() {
		
		// 探针 -> CommandProcessing过程
		{
			AtomicInteger amountOfRunning = new AtomicInteger(0);
			AtomicInteger amountOfPause = new AtomicInteger(0);
			AtomicInteger amountOfProcessing = new AtomicInteger(0);
			commandMailboxDict.entrySet().parallelStream().map(e -> {
				if(e.getValue().isRunning())
					amountOfRunning.incrementAndGet();
				AtomicBoolean onProcessing = fieldValue(dF_ProcessingCommandMailbox_onProcessing, e.getValue(), AtomicBoolean.class);
				if(onProcessing.get())
					amountOfProcessing.incrementAndGet();
				AtomicBoolean onPause = fieldValue(dF_ProcessingCommandMailbox_onPaused, e.getValue(), AtomicBoolean.class);
				if(onPause.get())
					amountOfPause.incrementAndGet();
				
				return 0;
			}).distinct().count(); // end stream
			
			logger.error("size of commandMailBoxDict: {}, which onRunning: {}, onProcessing: {}, onPause: {}",
					commandMailboxDict.size(),
					amountOfRunning.get(),
					amountOfProcessing.get(),
					amountOfPause.get()
					);
		}
		
		// 探针 -> EventCommitting过程
		{
			AtomicInteger amountOfRunning = new AtomicInteger(0);
			AtomicInteger amountOfProcessing = new AtomicInteger(0);
			AtomicInteger amountOfHasRemind = new AtomicInteger(0);
			AtomicInteger amountOfAggr = new AtomicInteger(0);
			eventCommittingContextMailBoxList.parallelStream().map(ecc -> {
				if(ecc.isRunning())
					amountOfRunning.incrementAndGet();
				if(ecc.hasRemindMessage())
					amountOfHasRemind.incrementAndGet();
//				AtomicBoolean onProcessing = fieldValue(dF_EventCommittingContextMailBox_onProcessing, ecc, AtomicBoolean.class);
//				if(onProcessing.get())
//					amountOfProcessing.incrementAndGet();
				
				Map<String, Map<String, Byte>> aggregateDictDict = (Map<String, Map<String, Byte>> )fieldValue(dF_EventCommittingContextMailBox_aggregateDictDict, ecc, Map.class);
				Set<String> aggrIds = aggregateDictDict.entrySet().parallelStream().map(Entry::getKey).distinct().collect(Collectors.toSet());
				amountOfAggr.getAndAdd(aggrIds.size());
				
				return 0;
				}).distinct().count(); // end stream
			logger.error("size of EventCommittingContextMailBox: {}, amountOfAggr: {}, which onRunning: {}, onProcessing: {}, amountOfHasRemind: {}",
					eventCommittingContextMailBoxList.size(), amountOfAggr.get(), amountOfRunning.get(), amountOfProcessing.get(), amountOfHasRemind.get());
			eventCommittingContextMailBoxList.forEach(ecc -> {
				if(ecc.hasRemindMessage())
					logger.error("\t\t boxIdex: {}, totalUnHandledMessageCount: {}", ecc.getNumber(), ecc.getTotalUnHandledMessageCount());
			});
		}

		// 探针 -> EventProcessing过程
		{
			Integer amountOfRunning = eventMailboxDict.entrySet().parallelStream().map(e -> e.getValue().isRunning()?1:0).reduce(0, (l, r) -> l + r);
			Integer amountOfHasRemind = eventMailboxDict.entrySet().parallelStream().map(e -> e.getValue().hasRemindMessage()?1:0).reduce(0, (l, r) -> l + r);
			Integer amountOfWaitting = eventMailboxDict.entrySet().parallelStream().map(e -> fieldValue(dF_ProcessingEventMailBox_waitingMessageDict, e.getValue(), Map.class).size()).reduce(0, (l, r) -> l + r);
			
			logger.error("size of ProcessingEventMailBox: {} which onRunning: {}, amountOfHasRemind: {}, amountOfWaitting: {}",
					eventMailboxDict.size(),
					amountOfRunning,
					amountOfHasRemind,
					amountOfWaitting);
			eventMailboxDict.entrySet().forEach(e -> {
				ProcessingEventMailBox mailBox = e.getValue();
				if(mailBox.isRunning() || mailBox.hasRemindMessage())
					logger.error("\t\t mainBox[aggrId={}]  ->  latestHandledEventVersion: {}, totalUnHandledMessageCount: {}, totalWaittingSize: {}",
							e.getKey(),
							mailBox.getLatestHandledEventVersion(),
							mailBox.getTotalUnHandledMessageCount(),
							fieldValue(dF_ProcessingEventMailBox_waitingMessageDict, mailBox, Map.class).size());
			});
		}

		// 输出内存事件库中的信息(如果使用内存事件库)
		if(null != mStorage) {
			logger.error("size of InMemoryEventStore: {}", mStorage.size());
			List<String> typeNames = mStorage.entrySet()
				.parallelStream()
				.map(e -> 0 < e.getValue().eventDict.size()
						? e.getValue().eventDict.get(e.getValue().currentVersion).getAggregateRootTypeName()
						: "")
				.distinct()
				.collect(Collectors.toList());
			for(String tName : typeNames) {
				if(null == tName || "".equals(tName))
					continue;
				final Map<String, AtomicLong> dict = new ConcurrentHashMap<>();
				mStorage.entrySet()
					.parallelStream()
					.filter(e -> tName.equals(e.getValue().eventDict.get(e.getValue().currentVersion).getAggregateRootTypeName()))
					.map(e -> e.getValue().currentVersion)
					.mapToLong(l -> MapHelper.getOrAddConcurrent(dict, "" + l, () -> new AtomicLong()).incrementAndGet())
					.sum();
				logger.error("\t\t domain[type={}] version statistics: {}", tName, dict.toString());
			}
		}
		
		// 输出内存中记录量(如果使用内存版本库)
		if(null != versionDict) {
			logger.error("size of InMemoryPublishedVersionStore: {}", versionDict.size());
			if(!versionDict.isEmpty()) {
				versionDict.entrySet().parallelStream().map(e -> {
					
					return 0;
				});
				
				final Map<String, AtomicLong> dict = new ConcurrentHashMap<>();
				versionDict.entrySet().parallelStream().map(e -> {
					return e.getValue();
				}).mapToLong(l -> MapHelper.getOrAddConcurrent(dict, "" + l, () -> new AtomicLong()).incrementAndGet()).sum();
				logger.error("\t\t version statistics: {}", dict.toString());
			}
		}
		
	}
	
	// ======================  

	Field dF_ProcessingCommandMailbox_onProcessing = null;
	Field dF_ProcessingCommandMailbox_onPaused = null;
	Field dF_ProcessingCommandMailbox_messageDict = null;
	
//	Field dF_EventCommittingContextMailBox_onProcessing = null;
	Field dF_EventCommittingContextMailBox_aggregateDictDict = null;
	
	Field dF_ProcessingEventMailBox_waitingMessageDict = null;

	@EInitialize
	private void init() {
		
		connect(commandProcessor, "mailboxDict", "commandMailboxDict");
		
		connect(eventService, "eventCommittingContextMailBoxList");
		
		connect(processingEventProcessor, "mailboxDict", "eventMailboxDict");
		
		if(eventStore instanceof InMemoryEventStore)
			connect(eventStore, "mStorage");
		
		if(inMemoryPublishedVersionStore instanceof InMemoryPublishedVersionStore)
			connect(inMemoryPublishedVersionStore, "versionDict");
	}

}
