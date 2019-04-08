package pro.jiefzz.ejoker.demo.simple.transfer.boot;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.queue.applicationMessage.ApplicationMessageConsumer;
import com.jiefzz.ejoker.queue.applicationMessage.ApplicationMessagePublisher;
import com.jiefzz.ejoker.queue.command.CommandConsumer;
import com.jiefzz.ejoker.queue.command.CommandResultProcessor;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.queue.domainEvent.DomainEventConsumer;
import com.jiefzz.ejoker.queue.domainEvent.DomainEventPublisher;
import com.jiefzz.ejoker.queue.publishableExceptions.PublishableExceptionConsumer;
import com.jiefzz.ejoker.queue.publishableExceptions.PublishableExceptionPublisher;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.IEjokerContextDev2;
import com.jiefzz.ejoker.z.common.scavenger.Scavenger;
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;
import com.jiefzz.ejoker_support.rocketmq.MQInstanceHelper;

public class EJokerBootstrap {
	
	private final static  Logger logger = LoggerFactory.getLogger(EJokerBootstrap.class);

	protected final static String EJokerDefaultImplPackage = "com.jiefzz.ejoker_support.defaultMemoryImpl";
//	protected final static String EJokerDefaultImplPackage = "pro.jiefzz.ejoker.demo.completion.mongo.mongoSync";
	
	protected final static String BusinessPackage = "pro.jiefzz.ejoker.demo.simple.transfer";
	
	public final static String NameServAddr = "test_rocketmq_2:9876;test_sit_1:9876";
	
	public final static String EJokerDomainEventGroup = "EjokerDomainEventGroup";
	
	public final static String EJokerCommandGroup = "EjokerCommandGroup";

	public final static String EJokerApplicationMessageGroup = "EjokerApplicationMessageGroup";

	public final static String EJokerPublishableExceptionGroup = "EjokerPublishableExceptionGroup";
	
	
	public final static long BatchDelay = 10000l;
	
	protected final IEJokerSimpleContext eJokerContext;

	protected final Scavenger scavenger;
	
	protected final SystemAsyncHelper systemAsyncHelper;
	
	protected final AtomicBoolean[] cTables = new AtomicBoolean[] {
			new AtomicBoolean(false),	// 0 domain event consumer
			new AtomicBoolean(false),	// 1 domain event producer
			new AtomicBoolean(false),	// 2 command consumer
			new AtomicBoolean(false),	// 3 command producer
			new AtomicBoolean(false),	// 4 application message consumer
			new AtomicBoolean(false),	// 5 application message producer
			new AtomicBoolean(false),	// 6 publishable exception consumer
			new AtomicBoolean(false),	// 7 publishable exception producer
	};

	public EJokerBootstrap() {
		this(EJoker.getInstance());
	}
	
	protected EJokerBootstrap(EJoker eJokerInstance) {

		logger.info("====================== EJokerFramework ======================");
		logger.info("eJoker context initializing ... ");
		
		eJokerContext = eJokerInstance.getEJokerContext();
		
		{
			IEjokerContextDev2 eJokerFullContext = (IEjokerContextDev2 )eJokerContext;
			eJokerFullContext.scanPackage(EJokerDefaultImplPackage);
			eJokerFullContext.scanPackage(BusinessPackage);
			eJokerFullContext.refresh();
		}

		scavenger = eJokerContext.get(Scavenger.class);
		systemAsyncHelper = eJokerContext.get(SystemAsyncHelper.class);
	}
	
	public IEJokerSimpleContext getEJokerContext() {
		return eJokerContext;
	}

	public void discard() {
		IEjokerContextDev2 eJokerFullContext = (IEjokerContextDev2 )eJokerContext;
		eJokerFullContext.discard();
	}
	
	public void initAll() throws Exception {
		this.initCommandService();
		this.initDomainEventPublisher();
		this.initApplicationMessageProducer();
		this.initPublishableExceptionProducer();
		
		this.initPublishableExceptionConsumer();
		this.initApplicationMessageConsumer();
		this.initDomainEventConsumer();
		this.initCommandConsumer();
	}
	
	private CommandResultProcessor initCommandResultProcessor() {

		// 启动命令跟踪反馈控制对象
		CommandResultProcessor commandResultProcessor = eJokerContext.get(CommandResultProcessor.class);
		commandResultProcessor.start();
		return commandResultProcessor;
		
	}
	
	/* ========================= */
	
	public DomainEventConsumer initDomainEventConsumer() throws Exception {

		// 启动领域事件消费者
		DomainEventConsumer domainEventConsumer = eJokerContext.get(DomainEventConsumer.class);
		if(cTables[0].compareAndSet(false, true)) {
			domainEventConsumer
				.useConsumer(MQInstanceHelper.createDefaultMQConsumer(EJokerDomainEventGroup, NameServAddr, eJokerContext))
				.subscribe(TopicReference.DomainEventTopic)
				.start();
		}
		return domainEventConsumer;
		
	}
	
	public DomainEventPublisher initDomainEventPublisher() {

		// 启动领域事件发布者
		DomainEventPublisher domainEventPublisher = eJokerContext.get(DomainEventPublisher.class);
		if(cTables[1].compareAndSet(false, true)) {
			domainEventPublisher
				.useProducer(MQInstanceHelper.createDefaultMQProducer(EJokerDomainEventGroup, NameServAddr, eJokerContext))
				.start();
		}
		return domainEventPublisher;
		
	}
	
	/* ========================= */

	public CommandConsumer initCommandConsumer() throws Exception {

		// 启动命令消费者
		CommandConsumer commandConsumer = eJokerContext.get(CommandConsumer.class);
		if(cTables[2].compareAndSet(false, true)) {
			commandConsumer
				.useConsumer(MQInstanceHelper.createDefaultMQConsumer(EJokerCommandGroup, NameServAddr, eJokerContext))
				.subscribe(TopicReference.CommandTopic)
				.start();
		}
		return commandConsumer;
		
	}
	
	public CommandService initCommandService() {
		// 启动命令服务
		CommandService commandService = eJokerContext.get(CommandService.class);
		if(cTables[3].compareAndSet(false, true)) {
			initCommandResultProcessor();
			commandService
				.useProducer(MQInstanceHelper.createDefaultMQProducer(EJokerCommandGroup, NameServAddr, eJokerContext))
				.start();
		}
		return commandService;
	}
	
	/* ========================= */

	public ApplicationMessageConsumer initApplicationMessageConsumer() throws Exception {

		// 启动应用信息消费者
		ApplicationMessageConsumer applicationMessageConsumer = eJokerContext.get(ApplicationMessageConsumer.class);
		if(cTables[4].compareAndSet(false, true)) {
			applicationMessageConsumer
				.useConsumer(MQInstanceHelper.createDefaultMQConsumer(EJokerApplicationMessageGroup, NameServAddr, eJokerContext))
				.subscribe(TopicReference.ApplicationMessageTopic)
				.start();
		}
		return applicationMessageConsumer;
		
	}
	
	public ApplicationMessagePublisher initApplicationMessageProducer() {
		// 启动应用信息生产者
		ApplicationMessagePublisher applicationMessageProducer = eJokerContext.get(ApplicationMessagePublisher.class);
		if(cTables[5].compareAndSet(false, true)) {
			applicationMessageProducer
				.useProducer(MQInstanceHelper.createDefaultMQProducer(EJokerApplicationMessageGroup, NameServAddr, eJokerContext))
				.start();
		}
		return applicationMessageProducer;
	}
	
	/* ========================= */

	public PublishableExceptionConsumer initPublishableExceptionConsumer() throws Exception {

		// 启动可发布异常消费者
		PublishableExceptionConsumer publishableExceptionConsumer = eJokerContext.get(PublishableExceptionConsumer.class);
		if(cTables[6].compareAndSet(false, true)) {
			publishableExceptionConsumer
				.useConsumer(MQInstanceHelper.createDefaultMQConsumer(EJokerPublishableExceptionGroup, NameServAddr, eJokerContext))
				.subscribe(TopicReference.ExceptionTopic)
				.start();
		}
		return publishableExceptionConsumer;
		
	}
	
	public PublishableExceptionPublisher initPublishableExceptionProducer() {
		// 启动可发布异常生产者
		PublishableExceptionPublisher publishableExceptionProducer = eJokerContext.get(PublishableExceptionPublisher.class);
		if(cTables[7].compareAndSet(false, true)) {
			publishableExceptionProducer
				.useProducer(MQInstanceHelper.createDefaultMQProducer(EJokerPublishableExceptionGroup, NameServAddr, eJokerContext))
				.start();
		}
		return publishableExceptionProducer;
	}
	
}
