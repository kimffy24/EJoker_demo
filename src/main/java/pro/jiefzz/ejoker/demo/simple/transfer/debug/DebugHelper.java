package pro.jiefzz.ejoker.demo.simple.transfer.debug;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	static final Field declaredField;
	static final Field declaredField2;
	static final Field declaredField3;
	static final Field declaredField_DefaultCommandProcessor_mailboxDict;
	static final Field declaredField_DefaultEventService_eventMailboxDict;
	static final Field declaredField_AbstractDefaultMessageProcessor_mailboxDict;
	
	
	
	static {
		try {
			declaredField = InMemoryPublishedVersionStore.class.getDeclaredField("versionDict");
			declaredField2 = DefaultMQConsumer.class.getDeclaredField("dashboards");
			declaredField3 = InMemoryEventStore.class.getDeclaredField("mStorage");
			declaredField_DefaultCommandProcessor_mailboxDict = DefaultCommandProcessor.class.getDeclaredField("mailboxDict");
			declaredField_DefaultEventService_eventMailboxDict = DefaultEventService.class.getDeclaredField("eventMailboxDict");
			declaredField_AbstractDefaultMessageProcessor_mailboxDict = AbstractDefaultMessageProcessor.class.getDeclaredField("mailboxDict");
		} catch (NoSuchFieldException|SecurityException e) {
			throw new RuntimeException(e);
		}
		declaredField.setAccessible(true);
		declaredField2.setAccessible(true);
		declaredField3.setAccessible(true);
		declaredField_DefaultCommandProcessor_mailboxDict.setAccessible(true);
		declaredField_DefaultEventService_eventMailboxDict.setAccessible(true);
		declaredField_AbstractDefaultMessageProcessor_mailboxDict.setAccessible(true);
	}
	
	private Map<MessageQueue, ControlStruct> fetchEvtDashboards() {
			Object object3;
			{
		
				try {
					object3 = declaredField2.get(eventConsumer.getDeeplyConsumer());
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
					object2 = declaredField2.get(commandConsumer.getDeeplyConsumer());
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
				object4 = declaredField3.get(eventStore);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		mStorage = (Map<String, Map<String, DomainEventStream>> )object4;


		Object object;
		{
			try {
				object = declaredField.get(inMemoryPublishedVersionStore);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		versionDict = (Map<String, Long> )object;

	}
	
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage dm) {
		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}

	
	public SystemFutureWrapper<AsyncTaskResult<Void>> handleAsync(DebugMessage2 dm) {
		
		final String aggregateRootId = dm.aId;
		
		getCommandMailBoxDict().entrySet().parallelStream().forEach(e -> {
			if(aggregateRootId.equals(e.getKey())) {
				ProcessingCommandMailbox box = e.getValue();
				Set<Long> keySet = box.messageDict.keySet();
				Set<Long> keySet2 = box.requestToCompleteCommandDict.keySet();
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
						box.enqueueLock, box.asyncLock, keySet.toString(), keySet2.toString(),
						box.nextSequence, box.consumingSequence, box.consumedSequence, box.onRunning(),
						box.onPaused.get(), box.onProcessingFlag().get(), box.lastActiveTime
						);
			}
		});
		
		getEventMailBoxDict().entrySet().parallelStream().forEach(e -> {
			if(aggregateRootId.equals(e.getKey())) {
				EventMailBox box = e.getValue();
				String nextCommandInfo = null;
				EventCommittingContext cxt = box.messageQueue.peek();
				if(null != cxt) {
					ProcessingCommand processingCommand = cxt.getProcessingCommand();
					nextCommandInfo = String.format("cmdId: %s, cmdSequence: %d", processingCommand.getMessage().getId(), processingCommand.getSequence());
				}
				logger.error("eventMailBoxDict: \n\tmessageQueue.size(): {},"
						+ "\n\tnextCommandInfo: {},"
						+ "\n\tonRunning.get(): {},"
						+ "\n\tlastActiveTime: {}",
						box.messageQueue.size(), nextCommandInfo, box.isRunning(), box.getLastActiveTime()
						);
			}
		});
		
		getProcessingMessageMailBoxDict().entrySet().parallelStream().forEach(e -> {
			if(aggregateRootId.equals(e.getKey())) {
				ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> value = e.getValue();
				Set<Long> keySet;
				if(null != value.waitingMessageDict)
					keySet = value.waitingMessageDict.keySet();
				else
					keySet = new HashSet<>();
				logger.error("processingMessageMailBox: \n\troutingKey: {}, "
						+ "\n\tmessageQueue.size():, {}"
						+ "\n\twaitingMessageDict: {}, "
						+ "\n\tonRunning.get(): {}, "
						+ "\n\tlastActiveTime: {}",
						value.routingKey, value.messageQueue.size(), keySet.toString(), value.onRunning.get(), value.lastActiveTime
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
		
		getProcessingMessageMailBoxDict().entrySet().parallelStream().forEach(e -> {
			ProcessingMessageMailbox<ProcessingDomainEventStreamMessage,DomainEventStreamMessage> value = e.getValue();
			processingMessageScheduler.scheduleMailbox(value);
		});
		
		getEventMailBoxDict().entrySet().parallelStream().forEach(e -> {
			EventMailBox value = e.getValue();
			value.tryRun();
		});
		
		getCommandMailBoxDict().entrySet().parallelStream().forEach(e -> {
			ProcessingCommandMailbox value = e.getValue();
			value.tryRun();
		});
		
		logger.error("===================================================================================================== ok !!!!");
		
		return SystemFutureWrapperUtil.createCompleteFutureTask();
	}
	
}
