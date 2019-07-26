package pro.jiefzz.ejoker.demo.simple.transfer.debug;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.commanding.CommandResult;
import com.jiefzz.ejoker.commanding.ICommandProcessor;
import com.jiefzz.ejoker.commanding.ProcessingCommand;
import com.jiefzz.ejoker.commanding.ProcessingCommandMailbox;
import com.jiefzz.ejoker.commanding.impl.DefaultCommandProcessor;
import com.jiefzz.ejoker.eventing.DomainEventStream;
import com.jiefzz.ejoker.eventing.DomainEventStreamMessage;
import com.jiefzz.ejoker.eventing.EventCommittingContext;
import com.jiefzz.ejoker.eventing.IEventService;
import com.jiefzz.ejoker.eventing.IEventStore;
import com.jiefzz.ejoker.eventing.impl.DefaultEventService;
import com.jiefzz.ejoker.eventing.impl.EventMailBox;
import com.jiefzz.ejoker.eventing.impl.InMemoryEventStore;
import com.jiefzz.ejoker.infrastructure.AbstractMailBox;
import com.jiefzz.ejoker.infrastructure.IProcessingMessageScheduler;
import com.jiefzz.ejoker.infrastructure.IPublishedVersionStore;
import com.jiefzz.ejoker.infrastructure.ProcessingMessageMailbox;
import com.jiefzz.ejoker.infrastructure.impl.AbstractDefaultMessageProcessor;
import com.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import com.jiefzz.ejoker.infrastructure.impl.InMemoryPublishedVersionStore;
import com.jiefzz.ejoker.infrastructure.varieties.domainEventStreamMessage.ProcessingDomainEventStreamMessage;
import com.jiefzz.ejoker.z.common.context.annotation.assemblies.MessageHandler;
import com.jiefzz.ejoker.z.common.context.annotation.context.Dependence;
import com.jiefzz.ejoker.z.common.context.annotation.context.EInitialize;
import com.jiefzz.ejoker.z.common.context.annotation.context.EService;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.io.IOHelper;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapper;
import com.jiefzz.ejoker.z.common.system.extension.acrossSupport.SystemFutureWrapperUtil;
import com.jiefzz.ejoker.z.common.system.helper.MapHelper;
import com.jiefzz.ejoker.z.common.task.AsyncTaskResult;
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;

@MessageHandler
@EService
public class DebugHelper extends AbstractMessageHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(DebugHelper.class);

	@Dependence
	IEJokerSimpleContext eJokerContext;
	
	@Dependence
	IScheduleService scheduleService;

	@Dependence
	IPublishedVersionStore inMemoryPublishedVersionStore;
	
	@Dependence
	IEventStore eventStore;
	
	@Dependence
	IOHelper ioHelper;
	
	@Dependence
	ICommandProcessor commandProcessor;

	@Dependence
	IEventService eventService;
	
	@Dependence
	AbstractDefaultMessageProcessor<ProcessingDomainEventStreamMessage, DomainEventStreamMessage> domainEventStreamMessageProcessor;
	
	@Dependence
	IProcessingMessageScheduler<ProcessingDomainEventStreamMessage, DomainEventStreamMessage> processingMessageScheduler;
	
	@Dependence
	SystemAsyncHelper systemAsyncHelper;
	
	Map<String, Long> versionDict = null;
	
	Map<String, Map<String, DomainEventStream>> mStorage = null;
	
	static final Field dF_DefaultCommandProcessor_mailboxDict;
	
//	static final Field dF_DefaultEventService_eventMailboxDict;
	
	static final Field dF_AbstractDefaultMessageProcessor_mailboxDict;

	static final Field dF_ProcessingMessageMailbox_routingKey;
	static final Field dF_ProcessingMessageMailbox_messageQueue;
	static final Field dF_ProcessingMessageMailbox_waitingMessageDict;
	static final Field dF_ProcessingMessageMailbox_onRunning;
	static final Field dF_ProcessingMessageMailbox_lastActiveTime;

//	static final Field dF_AbstractAggregateMessageMailBox_aggregateRootId;
	static final Field dF_AbstractAggregateMessageMailBox_messageDict;
	static final Field dF_AbstractAggregateMessageMailBox_requestToCompleteMessageDict;
	static final Field dF_AbstractAggregateMessageMailBox_enqueueLock;
	static final Field dF_AbstractAggregateMessageMailBox_asyncLock;
	static final Field dF_AbstractAggregateMessageMailBox_nextSequence;
	static final Field dF_AbstractAggregateMessageMailBox_consumingSequence;
	static final Field dF_AbstractAggregateMessageMailBox_consumedSequence;
	static final Field dF_AbstractAggregateMessageMailBox_onRunning;
	static final Field dF_AbstractAggregateMessageMailBox_onPaused;
	static final Field dF_AbstractAggregateMessageMailBox_onProcessing;
	static final Field dF_AbstractAggregateMessageMailBox_lastActiveTime;
	
	static final Field dF_InMemoryPublishedVersionStore_versionDict;
	
	static final Field dF_InMemoryEventStore_mStorage;

	static {
		try {
			dF_DefaultCommandProcessor_mailboxDict = DefaultCommandProcessor.class.getDeclaredField("mailboxDict");
			
//			dF_DefaultEventService_eventMailboxDict = DefaultEventService.class.getDeclaredField("eventMailboxDict");
			
			dF_AbstractDefaultMessageProcessor_mailboxDict = AbstractDefaultMessageProcessor.class.getDeclaredField("mailboxDict");
			
			dF_ProcessingMessageMailbox_waitingMessageDict = ProcessingMessageMailbox.class.getDeclaredField("waitingMessageDict");
			dF_ProcessingMessageMailbox_routingKey = ProcessingMessageMailbox.class.getDeclaredField("routingKey");
			dF_ProcessingMessageMailbox_messageQueue = ProcessingMessageMailbox.class.getDeclaredField("messageQueue");
			dF_ProcessingMessageMailbox_lastActiveTime = ProcessingMessageMailbox.class.getDeclaredField("lastActiveTime");
			dF_ProcessingMessageMailbox_onRunning = ProcessingMessageMailbox.class.getDeclaredField("onRunning");

//			dF_AbstractAggregateMessageMailBox_aggregateRootId = AbstractMailBox.class.getDeclaredField("aggregateRootId");
			dF_AbstractAggregateMessageMailBox_messageDict = AbstractMailBox.class.getDeclaredField("messageDict");
			dF_AbstractAggregateMessageMailBox_requestToCompleteMessageDict = AbstractMailBox.class.getDeclaredField("requestToCompleteMessageDict");
			dF_AbstractAggregateMessageMailBox_enqueueLock = AbstractMailBox.class.getDeclaredField("enqueueLock");
			dF_AbstractAggregateMessageMailBox_asyncLock = AbstractMailBox.class.getDeclaredField("asyncLock");
			dF_AbstractAggregateMessageMailBox_nextSequence = AbstractMailBox.class.getDeclaredField("nextSequence");
			dF_AbstractAggregateMessageMailBox_consumingSequence = AbstractMailBox.class.getDeclaredField("consumingSequence");
			dF_AbstractAggregateMessageMailBox_consumedSequence = AbstractMailBox.class.getDeclaredField("consumedSequence");
			dF_AbstractAggregateMessageMailBox_onRunning = AbstractMailBox.class.getDeclaredField("onRunning");
			dF_AbstractAggregateMessageMailBox_onPaused = AbstractMailBox.class.getDeclaredField("onPaused");
			dF_AbstractAggregateMessageMailBox_onProcessing = AbstractMailBox.class.getDeclaredField("onProcessing");
			dF_AbstractAggregateMessageMailBox_lastActiveTime = AbstractMailBox.class.getDeclaredField("lastActiveTime");

			dF_InMemoryPublishedVersionStore_versionDict = InMemoryPublishedVersionStore.class.getDeclaredField("versionDict");
			
			dF_InMemoryEventStore_mStorage = InMemoryEventStore.class.getDeclaredField("mStorage");
		} catch (NoSuchFieldException|SecurityException e) {
			throw new RuntimeException(e);
		}
		
		dF_DefaultCommandProcessor_mailboxDict.setAccessible(true);
		
//		dF_DefaultEventService_eventMailboxDict.setAccessible(true);
		
		dF_AbstractDefaultMessageProcessor_mailboxDict.setAccessible(true);
		
		dF_ProcessingMessageMailbox_waitingMessageDict.setAccessible(true);
		dF_ProcessingMessageMailbox_routingKey.setAccessible(true);
		dF_ProcessingMessageMailbox_messageQueue.setAccessible(true);
		dF_ProcessingMessageMailbox_lastActiveTime.setAccessible(true);
		dF_ProcessingMessageMailbox_onRunning.setAccessible(true);

//		dF_AbstractAggregateMessageMailBox_aggregateRootId.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_messageDict.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_requestToCompleteMessageDict.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_enqueueLock.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_asyncLock.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_nextSequence.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_consumingSequence.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_consumedSequence.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_onRunning.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_onPaused.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_onProcessing.setAccessible(true);
		dF_AbstractAggregateMessageMailBox_lastActiveTime.setAccessible(true);

		dF_InMemoryPublishedVersionStore_versionDict.setAccessible(true);
		
		dF_InMemoryEventStore_mStorage.setAccessible(true);
		
	}

	@SuppressWarnings("unchecked")
	private Map<String, ProcessingCommandMailbox> getCommandMailBoxDict() {
		Object object;
		{
	
			try {
				object = dF_DefaultCommandProcessor_mailboxDict.get(commandProcessor);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return (Map<String, ProcessingCommandMailbox> )object;
	}

//	@SuppressWarnings("unchecked")
//	private Map<String, EventMailBox> getEventMailBoxDict() {
//		Object object;
//		{
//			try {
//				object = dF_DefaultEventService_eventMailboxDict.get(eventService);
//			} catch (IllegalArgumentException|IllegalAccessException e) {
//				throw new RuntimeException(e);
//			}
//		}
//		return (Map<String, EventMailBox> )object;
//	}

	@SuppressWarnings("unchecked")
	private Map<String, ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>> getProcessingMessageMailBoxDict() {
		Object object;
		{
			try {
				object = dF_AbstractDefaultMessageProcessor_mailboxDict.get(domainEventStreamMessageProcessor);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return (Map<String, ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>> )object;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Map<String, DomainEventStream>>  getDomainEventMemoryStore() {
		Object obj;
		{
			try {
				obj = dF_InMemoryEventStore_mStorage.get(eventStore);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return (Map<String, Map<String, DomainEventStream>> )obj;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Long> getInMemoryPublishedVersionDict() {
		Object object;
		{
			try {
				object = dF_InMemoryPublishedVersionStore_versionDict.get(inMemoryPublishedVersionStore);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return (Map<String, Long> )object;
	}
	
	/**
	 * 列出某个聚合的领域基础设施中的所有状态和属性
	 * @param aggregateRootId 目标的聚合根ID
	 */
	private void profileAggregateProperties(final String aggregateRootId) {

		// 提取并输出CommandMailbox的状态和属性
		Optional<Entry<String, ProcessingCommandMailbox>> findFirst = getCommandMailBoxDict().entrySet()
				.parallelStream().filter(e -> aggregateRootId.equals(e.getKey())).findFirst();
		if (findFirst.isPresent()) {
			Entry<String, ProcessingCommandMailbox> processingCommandMailboxEntry = findFirst.get();
			ProcessingCommandMailbox box = processingCommandMailboxEntry.getValue();
			long consumingSequence;
			long consumedSequence;
			Map<Long, ProcessingCommand> messageDict;
			Map<Long, CommandResult> requestToCompleteCommandDict;
			Object enqueueLock;
			Object asyncLock;
			long nextSequence;
			long lastActiveTime;
			AtomicBoolean onRunning;
			AtomicBoolean onPaused;
			AtomicBoolean onProcessing;
			try {
				messageDict = (Map<Long, ProcessingCommand>) dF_AbstractAggregateMessageMailBox_messageDict.get(box);
				requestToCompleteCommandDict = (Map<Long, CommandResult>) dF_AbstractAggregateMessageMailBox_requestToCompleteMessageDict.get(box);
				enqueueLock = dF_AbstractAggregateMessageMailBox_enqueueLock.get(box);
				asyncLock = dF_AbstractAggregateMessageMailBox_asyncLock.get(box);
				nextSequence = (long) dF_AbstractAggregateMessageMailBox_nextSequence.get(box);
				consumingSequence = (long) dF_AbstractAggregateMessageMailBox_consumingSequence.get(box);
				consumedSequence = (long) dF_AbstractAggregateMessageMailBox_consumedSequence.get(box);

				onRunning = (AtomicBoolean) dF_AbstractAggregateMessageMailBox_onRunning.get(box);
				onPaused = (AtomicBoolean) dF_AbstractAggregateMessageMailBox_onPaused.get(box);
				onProcessing = (AtomicBoolean) dF_AbstractAggregateMessageMailBox_onProcessing.get(box);

				lastActiveTime = (long) dF_AbstractAggregateMessageMailBox_lastActiveTime.get(box);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				logger.error("ProcessingCommandMailbox[aggregateRootId={}] cannot be profile!!!", aggregateRootId);
				logger.error("", e1);
				throw new RuntimeException(e1);
			}

			Set<Long> keySet = messageDict.keySet();
			Set<Long> keySet2 = requestToCompleteCommandDict.keySet();
			logger.error(
					"processingCommandMailbox: \n\tenqueueLock: {}, " + "\n\tasyncLock: {},"
							+ "\n\tmessageDict.keys: {}," + "\n\trequestToCompleteCommandDict.keys: {},"
							+ "\n\tnextSequence: {}," + "\n\tconsumingSequence: {}," + "\n\tconsumedSequence: {},"
							+ "\n\tonRunning.get(): {}," + "\n\tonPaused.get(): {}," + "\n\tonProcessing.get(): {},"
							+ "\n\tlastActiveTime: {}",
					enqueueLock, asyncLock, keySet.toString(), keySet2.toString(), nextSequence, consumingSequence,
					consumedSequence, box.isRunning(), onPaused.get(), onProcessing.get(), lastActiveTime);
		}
		
//		// 提取并输出EventMailbox的状态和属性
//		Optional<Entry<String, EventMailBox>> findEventMailBox = getEventMailBoxDict().entrySet()
//				.parallelStream().filter(e -> aggregateRootId.equals(e.getKey())).findFirst();
//		if (findEventMailBox.isPresent()) {
//			Entry<String, EventMailBox> EventMailBoxEntry = findEventMailBox.get();
//			EventMailBox box = EventMailBoxEntry.getValue();
//			long consumingSequence;
//			long consumedSequence;
//			Map<Long, EventCommittingContext> messageDict;
//			Map<Long, Void> requestToCompleteCommandDict;
//			Object enqueueLock;
//			Object asyncLock;
//			long nextSequence;
//			long lastActiveTime;
//			AtomicBoolean onRunning;
//			AtomicBoolean onPaused;
//			AtomicBoolean onProcessing;
//			try {
//				messageDict = (Map<Long, EventCommittingContext>) dF_AbstractAggregateMessageMailBox_messageDict.get(box);
//				requestToCompleteCommandDict = (Map<Long, Void>) dF_AbstractAggregateMessageMailBox_requestToCompleteMessageDict.get(box);
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
//				logger.error("EventMailBox[aggregateRootId={}] cannot be profile!!!", aggregateRootId);
//				logger.error("", e1);
//				throw new RuntimeException(e1);
//			}
//
//			Set<Long> keySet = messageDict.keySet();
//			Set<Long> keySet2 = requestToCompleteCommandDict.keySet();
//			logger.error(
//					"eventMailbox: \n\tenqueueLock: {}, " + "\n\tasyncLock: {},"
//							+ "\n\tmessageDict.keys: {}," + "\n\trequestToCompleteCommandDict.keys: {},"
//							+ "\n\tnextSequence: {}," + "\n\tconsumingSequence: {}," + "\n\tconsumedSequence: {},"
//							+ "\n\tonRunning.get(): {}," + "\n\tonPaused.get(): {}," + "\n\tonProcessing.get(): {},"
//							+ "\n\tlastActiveTime: {}",
//					enqueueLock, asyncLock, keySet.toString(), keySet2.toString(), nextSequence, consumingSequence,
//					consumedSequence, box.isRunning(), onPaused.get(), onProcessing.get(), lastActiveTime);
//		}

		// 提取并输出Q端MessageMailbox的状态和属性
		Optional<Entry<String, ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>>> findProcessingMessageMailbox = getProcessingMessageMailBoxDict()
				.entrySet().parallelStream().filter(e -> aggregateRootId.equals(e.getKey())).findFirst();
		if (findProcessingMessageMailbox.isPresent()) {
			ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage> value = (ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>) findProcessingMessageMailbox
					.get();
			Set<Long> keySet;
			Map<Long, ProcessingDomainEventStreamMessage> map = getWaitingMessageDict(value);
			String routingKey = getProcessingMessageMailboxRroutingKey(value);
			Queue<ProcessingDomainEventStreamMessage> messageQueue = getMessageQueue(value);
			if (null != map)
				keySet = map.keySet();
			else
				keySet = new HashSet<>();

			long lastActiveTime;
			try {
				lastActiveTime = (long) dF_ProcessingMessageMailbox_lastActiveTime.get(value);
			} catch (IllegalArgumentException | IllegalAccessException e1) {
				logger.error("ProcessingCommandMailbox[aggregateRootId={}] cannot be probe!!!", aggregateRootId);
				logger.error("", e1);
				throw new RuntimeException(e1);
			}

			logger.error(
					"processingMessageMailBox: \n\troutingKey: {}, " + "\n\tmessageQueue.size():, {}"
							+ "\n\twaitingMessageDict: {}, " + "\n\tonRunning.get(): {}, " + "\n\tlastActiveTime: {}",
					routingKey, messageQueue.size(), keySet.toString(), value.onRunning(), lastActiveTime);
		}
		
		// 输出内存中记录的版本号(如果使用内存版本库)
		if("InMemoryPublishedVersionStore".equals(inMemoryPublishedVersionStore.getClass().getSimpleName()))
			logger.error("inMemoryPublishedVersionStore: \n\tversionDict: {}", versionDict.get("defaultEventProcessor-" + aggregateRootId));
		
		// 输出内存事件库中的事件(如果使用内存事件库)
		if("InMemoryEventStore".equals(eventStore.getClass().getSimpleName())) {
			Map<String, DomainEventStream> map = mStorage.get(aggregateRootId);
			if(null != map)
				logger.error("{}", map.toString());
		}
	}
	
	/**
	 * 获取Q端事件流信箱的routingKey
	 * @param target
	 * @return
	 */
	private String getProcessingMessageMailboxRroutingKey(ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> target) {
		try {
			return (String )dF_ProcessingMessageMailbox_routingKey.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 获得Q端事件流信箱的待处理队列
	 * @param target
	 * @return
	 */
	private Queue<ProcessingDomainEventStreamMessage> getMessageQueue(ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> target) {
		try {
			return (Queue<ProcessingDomainEventStreamMessage> )dF_ProcessingMessageMailbox_messageQueue.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return new LinkedBlockingQueue<>();
		}
	}

	/**
	 * 获取Q端事件流信箱的延后处理集合
	 * @param target
	 * @return
	 */
	private Map<Long, ProcessingDomainEventStreamMessage> getWaitingMessageDict(ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> target) {
		try {
			return (Map<Long, ProcessingDomainEventStreamMessage> )dF_ProcessingMessageMailbox_waitingMessageDict.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}
	
	/**
	 * 无
	 * @param dm
	 * @return
	 */
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage dm) {
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	/**
	 * 列出某个聚合的领域基础设施中的所有状态和属性
	 * @param dm
	 * @return
	 */
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage2 dm) {

		profileAggregateProperties(dm.aId);
		
		return SystemFutureWrapperUtil.completeFutureTask();
	}

	/**
	 * 对dm3类型消息的响应
	 * * 调度每一个mailbox
	 * @param dm
	 * @return
	 */
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage3 dm) {
		
		getProcessingMessageMailBoxDict().entrySet().parallelStream().forEach(e -> {
			ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> value = e.getValue();
			processingMessageScheduler.scheduleMailbox(value);
		});
		
//		getEventMailBoxDict().entrySet().parallelStream().forEach(e -> {
//			EventMailBox value = e.getValue();
//			value.tryRun();
//		});
		
		getCommandMailBoxDict().entrySet().parallelStream().forEach(e -> {
			ProcessingCommandMailbox value = e.getValue();
			value.tryRun();
		});
		
		logger.error("===================================================================================================== ok !!!!");
		
		return SystemFutureWrapperUtil.completeFutureTask();
	}
	
	private void probe() {
		
		Map<String, ProcessingCommandMailbox> commandMailBoxDict = getCommandMailBoxDict();
		logger.error("size of commandMailBoxDict: {}", commandMailBoxDict.size());
		
//		Map<String, EventMailBox> eventMailBoxDict = getEventMailBoxDict();
//		logger.error("size of EventMailBox: {}", eventMailBoxDict.size());
		
		Map<String, ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>> processingMessageMailBoxDict = getProcessingMessageMailBoxDict();
		logger.error("size of ProcessingMessageMailbox: {}", processingMessageMailBoxDict.size());

		// 输出内存事件库中的存量(如果使用内存事件库)
		if("InMemoryEventStore".equals(eventStore.getClass().getSimpleName())) {
			logger.error("size of InMemoryEventStore: {}", mStorage.size());
		}
		
		// 输出内存中记录量(如果使用内存版本库)
		if("InMemoryPublishedVersionStore".equals(inMemoryPublishedVersionStore.getClass().getSimpleName())) {
			logger.error("size of InMemoryPublishedVersionStore: {}", versionDict.size());
			final Map<String, AtomicLong> dict = new ConcurrentHashMap<>();
			versionDict.entrySet().parallelStream().map(e -> {
				return e.getValue();
			}).mapToLong(l -> MapHelper.getOrAddConcurrent(dict, "" + l, AtomicLong::new).incrementAndGet()).sum();
			logger.error("version statistics: {}", dict.toString());
		}
		
		// 输出transfer的设置的统计数据
		DevUtils.moniterQ(eJokerContext);
		
	}
	
	@EInitialize
	private void init() {
		try {
			mStorage = getDomainEventMemoryStore();
			versionDict = getInMemoryPublishedVersionDict();
		} catch (RuntimeException ex) {}

		scheduleService.startTask("DebugHelper_probe", this::probe, 5000l, 5000l);
	}
	
}
