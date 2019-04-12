package pro.jiefzz.ejoker.demo.simple.transfer.debug;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.rocketmq.common.message.MessageQueue;
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
import com.jiefzz.ejoker.infrastructure.IProcessingMessageScheduler;
import com.jiefzz.ejoker.infrastructure.IPublishedVersionStore;
import com.jiefzz.ejoker.infrastructure.ProcessingMessageMailbox;
import com.jiefzz.ejoker.infrastructure.impl.AbstractDefaultMessageProcessor;
import com.jiefzz.ejoker.infrastructure.impl.AbstractMessageHandler;
import com.jiefzz.ejoker.infrastructure.impl.InMemoryPublishedVersionStore;
import com.jiefzz.ejoker.infrastructure.varieties.domainEventStreamMessage.ProcessingDomainEventStreamMessage;
import com.jiefzz.ejoker.queue.command.CommandConsumer;
import com.jiefzz.ejoker.queue.domainEvent.DomainEventConsumer;
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
import com.jiefzz.ejoker_support.rocketmq.DefaultMQConsumer;
import com.jiefzz.ejoker_support.rocketmq.DefaultMQConsumer.ControlStruct;

@MessageHandler
@EService
public class DebugHelper extends AbstractMessageHandler {

	@Dependence
	IEJokerSimpleContext eJokerContext;
	
	@Dependence
	IScheduleService scheduleService;

	@Dependence
	IPublishedVersionStore inMemoryPublishedVersionStore;
	
	@Dependence
	IEventStore eventStore;
	
	@Dependence
	CommandConsumer commandConsumer;
	
	@Dependence
	DomainEventConsumer eventConsumer;
	
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
	
	private final static Logger logger = LoggerFactory.getLogger(DebugHelper.class);

	static final Field declaredField_InMemoryPublishedVersionStore_versionDict;
	static final Field declaredField_DefaultMQConsumer_dashboards;
	static final Field declaredField_InMemoryEventStore_mStorage;
	static final Field declaredField_DefaultCommandProcessor_mailboxDict;
	static final Field declaredField_DefaultEventService_eventMailboxDict;
	static final Field declaredField_AbstractDefaultMessageProcessor_mailboxDict;
	static final Field declaredField_ProcessingMessageMailbox_waitingMessageDict;
	static final Field declaredField_ProcessingMessageMailbox_routingKey;
	static final Field declaredField_ProcessingMessageMailbox_messageQueue;
	static final Field declaredField_ProcessingMessageMailbox_lastActiveTime;
	
	static final Field declaredField_ProcessingCommandMailbox_messageDict;
	static final Field declaredField_ProcessingCommandMailbox_requestToCompleteCommandDict;
	static final Field declaredField_ProcessingCommandMailbox_enqueueLock;
	static final Field declaredField_ProcessingCommandMailbox_asyncLock;
	static final Field declaredField_ProcessingCommandMailbox_nextSequence;
	static final Field declaredField_ProcessingCommandMailbox_consumingSequence;
	static final Field declaredField_ProcessingCommandMailbox_consumedSequence;
	static final Field declaredField_ProcessingCommandMailbox_lastActiveTime;
	static final Field declaredField_EventMailBox_messageQueue;
	
	
	static {
		try {
			declaredField_InMemoryPublishedVersionStore_versionDict = InMemoryPublishedVersionStore.class.getDeclaredField("versionDict");
			declaredField_DefaultMQConsumer_dashboards = DefaultMQConsumer.class.getDeclaredField("dashboards");
			declaredField_InMemoryEventStore_mStorage = InMemoryEventStore.class.getDeclaredField("mStorage");
			declaredField_DefaultCommandProcessor_mailboxDict = DefaultCommandProcessor.class.getDeclaredField("mailboxDict");
			declaredField_DefaultEventService_eventMailboxDict = DefaultEventService.class.getDeclaredField("eventMailboxDict");
			declaredField_AbstractDefaultMessageProcessor_mailboxDict = AbstractDefaultMessageProcessor.class.getDeclaredField("mailboxDict");
			declaredField_ProcessingMessageMailbox_waitingMessageDict = ProcessingMessageMailbox.class.getDeclaredField("waitingMessageDict");
			declaredField_ProcessingMessageMailbox_routingKey = ProcessingMessageMailbox.class.getDeclaredField("routingKey");
			declaredField_ProcessingMessageMailbox_messageQueue = ProcessingMessageMailbox.class.getDeclaredField("messageQueue");
			declaredField_ProcessingMessageMailbox_lastActiveTime = ProcessingMessageMailbox.class.getDeclaredField("lastActiveTime");

			declaredField_ProcessingCommandMailbox_messageDict = ProcessingCommandMailbox.class.getDeclaredField("messageDict");
			declaredField_ProcessingCommandMailbox_requestToCompleteCommandDict = ProcessingCommandMailbox.class.getDeclaredField("requestToCompleteCommandDict");
			declaredField_ProcessingCommandMailbox_enqueueLock = ProcessingCommandMailbox.class.getDeclaredField("enqueueLock");
			declaredField_ProcessingCommandMailbox_asyncLock = ProcessingCommandMailbox.class.getDeclaredField("asyncLock");
			declaredField_ProcessingCommandMailbox_nextSequence = ProcessingCommandMailbox.class.getDeclaredField("nextSequence");
			declaredField_ProcessingCommandMailbox_consumingSequence = ProcessingCommandMailbox.class.getDeclaredField("consumingSequence");
			declaredField_ProcessingCommandMailbox_consumedSequence = ProcessingCommandMailbox.class.getDeclaredField("consumedSequence");
			declaredField_ProcessingCommandMailbox_lastActiveTime = ProcessingCommandMailbox.class.getDeclaredField("lastActiveTime");
			declaredField_EventMailBox_messageQueue = EventMailBox.class.getDeclaredField("messageQueue");

		} catch (NoSuchFieldException|SecurityException e) {
			throw new RuntimeException(e);
		}
		declaredField_InMemoryPublishedVersionStore_versionDict.setAccessible(true);
		declaredField_DefaultMQConsumer_dashboards.setAccessible(true);
		declaredField_InMemoryEventStore_mStorage.setAccessible(true);
		declaredField_DefaultCommandProcessor_mailboxDict.setAccessible(true);
		declaredField_DefaultEventService_eventMailboxDict.setAccessible(true);
		declaredField_AbstractDefaultMessageProcessor_mailboxDict.setAccessible(true);
		declaredField_ProcessingMessageMailbox_waitingMessageDict.setAccessible(true);
		declaredField_ProcessingMessageMailbox_routingKey.setAccessible(true);
		declaredField_ProcessingMessageMailbox_messageQueue.setAccessible(true);
		declaredField_ProcessingMessageMailbox_lastActiveTime.setAccessible(true);
		declaredField_EventMailBox_messageQueue.setAccessible(true);
		declaredField_ProcessingCommandMailbox_messageDict.setAccessible(true);
		declaredField_ProcessingCommandMailbox_requestToCompleteCommandDict.setAccessible(true);
		declaredField_ProcessingCommandMailbox_enqueueLock.setAccessible(true);
		declaredField_ProcessingCommandMailbox_asyncLock.setAccessible(true);
		declaredField_ProcessingCommandMailbox_nextSequence.setAccessible(true);
		declaredField_ProcessingCommandMailbox_consumingSequence.setAccessible(true);
		declaredField_ProcessingCommandMailbox_consumedSequence.setAccessible(true);
		declaredField_ProcessingCommandMailbox_lastActiveTime.setAccessible(true);
		declaredField_ProcessingCommandMailbox_asyncLock.setAccessible(true);
		
	}
	
	private void probe() {
		
		if(null ==  getEvtConsumer() || null == getCmdConsumer())
			return;
		
		try {
			DevUtils.moniterQ();
			
			logger.error("InMemoryPublishedVersionStore.versionDict.size = {}", versionDict.size());
			
			if(1+1 == 2)
				return;
			
			final Map<String, AtomicLong> dict = new ConcurrentHashMap<>();
			versionDict.entrySet().parallelStream().map(e -> {
				return e.getValue();
			}).mapToLong(l -> MapHelper.getOrAddConcurrent(dict, "" + l, AtomicLong::new).incrementAndGet()).sum();
			logger.error("version statistics: {}", dict.toString());
			
			fetchCmdDashboards().entrySet().forEach(e -> {
				ControlStruct value = e.getValue();

				// 这里不使用并行流，普通流就行了。
				value.aheadCompletion.keySet().stream().sorted().reduce(value.offsetConsumedLocal.get(), (a, b) -> {
					if(0l < a) {
						if(a + 1 == b) {
							;
						} else {
							logger.error("前值: {}, 后值: {}, 队列: {}", a, b, e.getKey().toString());
						}
					}
					return b;
				});
			});

			// 这里不使用并行流，普通流就行了。
			fetchEvtDashboards().entrySet().forEach(e -> {
				ControlStruct value = e.getValue();
	
				value.aheadCompletion.keySet().stream().sorted().reduce(value.offsetConsumedLocal.get(), (a, b) -> {
					if(0l < a) {
						if(a + 1 == b) {
							;
						} else {
							logger.error("前值: {}, 后值: {}, 队列: {}", a, b, e.getKey().toString());
						}
					}
					return b;
				});
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private DefaultMQConsumer getEvtConsumer() {
		Object deeplyConsumer = eventConsumer.getDeeplyConsumer();
		if(null != deeplyConsumer)
			return (DefaultMQConsumer )deeplyConsumer;
		return null;
	}
	
	private DefaultMQConsumer getCmdConsumer() {
		Object deeplyConsumer = commandConsumer.getDeeplyConsumer();
		if(null != deeplyConsumer)
			return (DefaultMQConsumer )deeplyConsumer;
		return null;
	}
	
	
	private Map<MessageQueue, ControlStruct> fetchEvtDashboards() {
			Object object3;
			{
				try {
					object3 = declaredField_DefaultMQConsumer_dashboards.get(eventConsumer.getDeeplyConsumer());
				} catch (IllegalArgumentException|IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			return (Map<MessageQueue, ControlStruct> )object3;
	}
	
	private Map<MessageQueue, ControlStruct> fetchCmdDashboards() {
			Object object2;
			{
				try {
					object2 = declaredField_DefaultMQConsumer_dashboards.get(commandConsumer.getDeeplyConsumer());
				} catch (IllegalArgumentException|IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			return (Map<MessageQueue, ControlStruct> )object2;
	}
	
	private Map<String, ProcessingCommandMailbox> getCommandMailBoxDict() {

		Object object4;
		{
	
			try {
				object4 = declaredField_DefaultCommandProcessor_mailboxDict.get(commandProcessor);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return (Map<String, ProcessingCommandMailbox> )object4;
	}

	private Map<String, EventMailBox> getEventMailBoxDict() {

		Object object;
		{
			try {
				object = declaredField_DefaultEventService_eventMailboxDict.get(eventService);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return (Map<String, EventMailBox> )object;
	}

	private Map<String, ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>> getProcessingMessageMailBoxDict() {

		Object object;
		{
			try {
				object = declaredField_AbstractDefaultMessageProcessor_mailboxDict.get(domainEventStreamMessageProcessor);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		return (Map<String, ProcessingMessageMailbox<ProcessingDomainEventStreamMessage, DomainEventStreamMessage>> )object;
	}
	
	@EInitialize
	private void init() {

		Object object4;
		{
	
			try {
				object4 = declaredField_InMemoryEventStore_mStorage.get(eventStore);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		mStorage = (Map<String, Map<String, DomainEventStream>> )object4;


		Object object;
		{
			try {
				object = declaredField_InMemoryPublishedVersionStore_versionDict.get(inMemoryPublishedVersionStore);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		versionDict = (Map<String, Long> )object;

		scheduleService.startTask("DebugHelper_probe", this::probe, 5000l, 5000l);
	}
	
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage dm) {
		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage2 dm) {
		
		final String aggregateRootId = dm.aId;
		
		getCommandMailBoxDict().entrySet().parallelStream().forEach(e -> {
			if(aggregateRootId.equals(e.getKey())) {
				
				ProcessingCommandMailbox box = e.getValue();

				long consumingSequence;
				long consumedSequence;
				Map<Long, ProcessingCommand> messageDict;
				Map<Long, CommandResult> requestToCompleteCommandDict;
				Object enqueueLock;
				Object asyncLock;
				long nextSequence;
				long lastActiveTime;
				try {
					messageDict = (Map<Long, ProcessingCommand> )declaredField_ProcessingCommandMailbox_messageDict.get(box);
					requestToCompleteCommandDict = (Map<Long, CommandResult> )declaredField_ProcessingCommandMailbox_requestToCompleteCommandDict.get(box);
					enqueueLock = declaredField_ProcessingCommandMailbox_enqueueLock.get(box);
					asyncLock = declaredField_ProcessingCommandMailbox_asyncLock.get(box);
					nextSequence = (long )declaredField_ProcessingCommandMailbox_nextSequence.get(box);
					consumingSequence = (long )declaredField_ProcessingCommandMailbox_consumingSequence.get(box);
					consumedSequence = (long )declaredField_ProcessingCommandMailbox_consumedSequence.get(box);
					lastActiveTime = (long )declaredField_ProcessingCommandMailbox_lastActiveTime.get(box);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					logger.error("ProcessingCommandMailbox[aggregateRootId={}] cannot be probe!!!", aggregateRootId);
					logger.error("", e1);
					throw new RuntimeException(e1);
				}
				
				
				Set<Long> keySet = messageDict.keySet();
				Set<Long> keySet2 = requestToCompleteCommandDict.keySet();
				logger.error("processingCommandMailbox: \n\tenqueueLock: {}, "
						+ "\n\tasyncLock: {},"
						+ "\n\tmessageDict.keys: {},"
						+ "\n\trequestToCompleteCommandDict.keys: {},"
						+ "\n\tnextSequence: {},"
						+ "\n\tconsumingSequence: {},"
						+ "\n\tconsumedSequence: {},"
						+ "\n\tonRunning.get(): {},"
						+ "\n\tonPaused.get(): {},"
						+ "\n\tonProcessing.get(): {},"
						+ "\n\tlastActiveTime: {}",
						enqueueLock, asyncLock, keySet.toString(), keySet2.toString(),
						nextSequence, consumingSequence, consumedSequence, box.onRunning(),
						box.onProcessingFlag().get(), box.onProcessingFlag().get(), lastActiveTime
						);
			}
		});
		
		getEventMailBoxDict().entrySet().parallelStream().forEach(e -> {
			if(aggregateRootId.equals(e.getKey())) {
				EventMailBox box = e.getValue();
				
				Queue<EventCommittingContext> messageQueue;
				try {
					messageQueue = (Queue<EventCommittingContext> )declaredField_EventMailBox_messageQueue.get(box);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					logger.error("EventMailBox[aggregateRoot={} cannot be probe!!!", aggregateRootId);
					logger.error("", e1);
					throw new RuntimeException(e1);
				}
				
				String nextCommandInfo = null;
				EventCommittingContext cxt = messageQueue.peek();
				if(null != cxt) {
					ProcessingCommand processingCommand = cxt.getProcessingCommand();
					nextCommandInfo = String.format("cmdId: %s, cmdSequence: %d", processingCommand.getMessage().getId(), processingCommand.getSequence());
				}
				logger.error("eventMailBoxDict: \n\tmessageQueue.size(): {},"
						+ "\n\tnextCommandInfo: {},"
						+ "\n\tonRunning.get(): {},"
						+ "\n\tlastActiveTime: {}",
						messageQueue.size(), nextCommandInfo, box.isRunning(), box.getLastActiveTime()
						);
			}
		});
		
		getProcessingMessageMailBoxDict().entrySet().parallelStream().forEach(e -> {
			if(aggregateRootId.equals(e.getKey())) {
				ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> value = e.getValue();
				Set<Long> keySet;
				Map<Long, ProcessingDomainEventStreamMessage> map = getWaitingMessageDict(value);
				String routingKey = getRroutingKey(value);
				Queue<ProcessingDomainEventStreamMessage> messageQueue = getMessageQueue(value);
				if(null != map)
					keySet = map.keySet();
				else
					keySet = new HashSet<>();

				long lastActiveTime;
				try {
					lastActiveTime = (long )declaredField_ProcessingMessageMailbox_lastActiveTime.get(value);
				} catch (IllegalArgumentException | IllegalAccessException e1) {
					logger.error("ProcessingCommandMailbox[aggregateRootId={}] cannot be probe!!!", aggregateRootId);
					logger.error("", e1);
					throw new RuntimeException(e1);
				}
				
				logger.error("processingMessageMailBox: \n\troutingKey: {}, "
						+ "\n\tmessageQueue.size():, {}"
						+ "\n\twaitingMessageDict: {}, "
						+ "\n\tonRunning.get(): {}, "
						+ "\n\tlastActiveTime: {}",
						routingKey, messageQueue.size(), keySet.toString(), value.onRunning(), lastActiveTime
						);
			}
		});
		
		logger.error("inMemoryPublishedVersionStore: \n\tversionDict: {}", versionDict.get("defaultEventProcessor-" + aggregateRootId));
		
		Map<String, DomainEventStream> map = mStorage.get(aggregateRootId);
		if(null != map)
			logger.error("{}", map.toString());
		
		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}

	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage3 dm) {
//		
//		getProcessingMessageMailBoxDict().entrySet().parallelStream().forEach(e -> {
//			ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> value = e.getValue();
//			processingMessageScheduler.scheduleMailbox(value);
//		});
//		
//		getEventMailBoxDict().entrySet().parallelStream().forEach(e -> {
//			EventMailBox value = e.getValue();
//			value.tryRun();
//		});
//		
//		getCommandMailBoxDict().entrySet().parallelStream().forEach(e -> {
//			ProcessingCommandMailbox value = e.getValue();
//			value.tryRun();
//		});
//		
//		logger.error("===================================================================================================== ok !!!!");
//		
		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}
	
	private Map<Long, ProcessingDomainEventStreamMessage> getWaitingMessageDict(ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> target) {
		try {
			return (Map<Long, ProcessingDomainEventStreamMessage> )declaredField_ProcessingMessageMailbox_waitingMessageDict.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}
	
	private String getRroutingKey(ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> target) {
		try {
			return (String )declaredField_ProcessingMessageMailbox_routingKey.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	private Queue<ProcessingDomainEventStreamMessage> getMessageQueue(ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> target) {
		try {
			return (Queue<ProcessingDomainEventStreamMessage> )declaredField_ProcessingMessageMailbox_messageQueue.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return new LinkedBlockingQueue<>();
		}
	}
	
}
