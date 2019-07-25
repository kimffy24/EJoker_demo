package pro.jiefzz.ejoker.demo.simple.transfer.boot;

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.queue.applicationMessage.ApplicationMessageConsumer;
import com.jiefzz.ejoker.queue.applicationMessage.ApplicationMessagePublisher;
import com.jiefzz.ejoker.queue.aware.IConsumerWrokerAware;
import com.jiefzz.ejoker.queue.aware.IProducerWrokerAware;
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

public abstract class AbstractEJokerBootstrap {
	
	private final static  Logger logger = LoggerFactory.getLogger(AbstractEJokerBootstrap.class);

	protected final static String EJokerDefaultImplPackage = "com.jiefzz.ejoker_support.defaultMemoryImpl";
//	protected final static String EJokerDefaultImplPackage = "pro.jiefzz.ejoker.demo.completion.mongo.mongoSync";
	
	protected final static String BusinessPackage = "pro.jiefzz.ejoker.demo.simple.transfer";
	
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

	public AbstractEJokerBootstrap() {
		this(EJoker.getInstance());
	}
	
	protected AbstractEJokerBootstrap(EJoker eJokerInstance) {

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
		//哪里开的就哪里关闭。
		scavenger.addFianllyJob(() -> {
			commandResultProcessor.shutdown();
		});
		return commandResultProcessor;
		
	}
	
	/* ========================= */
	
	public DomainEventConsumer initDomainEventConsumer() throws Exception {

		// 启动领域事件消费者
		DomainEventConsumer domainEventConsumer = eJokerContext.get(DomainEventConsumer.class);
		if(cTables[0].compareAndSet(false, true)) {
			domainEventConsumer
				.useConsumer(createDefaultMQConsumer(EJokerDomainEventGroup, eJokerContext))
				.subscribe(TopicReference.DomainEventTopic)
				.start();
			//哪里开的就哪里关闭。
			scavenger.addFianllyJob(() -> {
				domainEventConsumer.shutdown();
			});
		}
		return domainEventConsumer;
		
	}
	
	public DomainEventPublisher initDomainEventPublisher() {

		// 启动领域事件发布者
		DomainEventPublisher domainEventPublisher = eJokerContext.get(DomainEventPublisher.class);
		if(cTables[1].compareAndSet(false, true)) {
			domainEventPublisher
				.useProducer(createDefaultMQProducer(EJokerDomainEventGroup, eJokerContext))
				.start();
			//哪里开的就哪里关闭。
			scavenger.addFianllyJob(() -> {
				domainEventPublisher.shutdown();
			});
		}
		return domainEventPublisher;
		
	}
	
	/* ========================= */

	public CommandConsumer initCommandConsumer() throws Exception {

		// 启动命令消费者
		CommandConsumer commandConsumer = eJokerContext.get(CommandConsumer.class);
		if(cTables[2].compareAndSet(false, true)) {
			commandConsumer
				.useConsumer(createDefaultMQConsumer(EJokerCommandGroup, eJokerContext))
				.subscribe(TopicReference.CommandTopic)
				.start();
			//哪里开的就哪里关闭。
			scavenger.addFianllyJob(() -> {
				commandConsumer.shutdown();
			});
		}
		return commandConsumer;
		
	}
	
	public CommandService initCommandService() {
		// 启动命令服务
		CommandService commandService = eJokerContext.get(CommandService.class);
		if(cTables[3].compareAndSet(false, true)) {
			initCommandResultProcessor();
			commandService
				.useProducer(createDefaultMQProducer(EJokerCommandGroup, eJokerContext))
				.start();
			//哪里开的就哪里关闭。
			scavenger.addFianllyJob(() -> {
				commandService.shutdown();
			});
		}
		return commandService;
	}
	
	/* ========================= */

	public ApplicationMessageConsumer initApplicationMessageConsumer() throws Exception {

		// 启动应用信息消费者
		ApplicationMessageConsumer applicationMessageConsumer = eJokerContext.get(ApplicationMessageConsumer.class);
		if(cTables[4].compareAndSet(false, true)) {
			applicationMessageConsumer
				.useConsumer(createDefaultMQConsumer(EJokerApplicationMessageGroup, eJokerContext))
				.subscribe(TopicReference.ApplicationMessageTopic)
				.start();
			//哪里开的就哪里关闭。
			scavenger.addFianllyJob(() -> {
				applicationMessageConsumer.shutdown();
			});
		}
		return applicationMessageConsumer;
		
	}
	
	public ApplicationMessagePublisher initApplicationMessageProducer() {
		// 启动应用信息生产者
		ApplicationMessagePublisher applicationMessageProducer = eJokerContext.get(ApplicationMessagePublisher.class);
		if(cTables[5].compareAndSet(false, true)) {
			applicationMessageProducer
				.useProducer(createDefaultMQProducer(EJokerApplicationMessageGroup, eJokerContext))
				.start();
			//哪里开的就哪里关闭。
			scavenger.addFianllyJob(() -> {
				applicationMessageProducer.shutdown();
			});
		}
		return applicationMessageProducer;
	}
	
	/* ========================= */

	public PublishableExceptionConsumer initPublishableExceptionConsumer() throws Exception {

		// 启动可发布异常消费者
		PublishableExceptionConsumer publishableExceptionConsumer = eJokerContext.get(PublishableExceptionConsumer.class);
		if(cTables[6].compareAndSet(false, true)) {
			publishableExceptionConsumer
				.useConsumer(createDefaultMQConsumer(EJokerPublishableExceptionGroup, eJokerContext))
				.subscribe(TopicReference.ExceptionTopic)
				.start();
			//哪里开的就哪里关闭。
			scavenger.addFianllyJob(() -> {
				publishableExceptionConsumer.shutdown();
			});
		}
		return publishableExceptionConsumer;
		
	}
	
	public PublishableExceptionPublisher initPublishableExceptionProducer() {
		// 启动可发布异常生产者
		PublishableExceptionPublisher publishableExceptionProducer = eJokerContext.get(PublishableExceptionPublisher.class);
		if(cTables[7].compareAndSet(false, true)) {
			publishableExceptionProducer
				.useProducer(createDefaultMQProducer(EJokerPublishableExceptionGroup, eJokerContext))
				.start();
			//哪里开的就哪里关闭。
			scavenger.addFianllyJob(() -> {
				publishableExceptionProducer.shutdown();
			});
		}
		return publishableExceptionProducer;
	}
	
	protected abstract IConsumerWrokerAware createDefaultMQConsumer(String groupName, IEJokerSimpleContext eContext);
	
	protected abstract IProducerWrokerAware createDefaultMQProducer(String groupName, IEJokerSimpleContext eContext);
}
