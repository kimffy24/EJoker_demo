package pro.jiefzz.ejoker.demo.simple.transfer.boot;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.queue.applicationMessage.ApplicationMessageConsumer;
import com.jiefzz.ejoker.queue.applicationMessage.ApplicationMessagePublisher;
import com.jiefzz.ejoker.queue.command.CommandConsumer;
import com.jiefzz.ejoker.queue.command.CommandResultProcessor;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.queue.completation.DefaultMQConsumer;
import com.jiefzz.ejoker.queue.completation.DefaultMQProducer;
import com.jiefzz.ejoker.queue.domainEvent.DomainEventConsumer;
import com.jiefzz.ejoker.queue.domainEvent.DomainEventPublisher;
import com.jiefzz.ejoker.queue.publishableExceptions.PublishableExceptionConsumer;
import com.jiefzz.ejoker.queue.publishableExceptions.PublishableExceptionPublisher;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.IEjokerContextDev2;
import com.jiefzz.ejoker.z.common.scavenger.Scavenger;
import com.jiefzz.ejoker.z.common.system.functional.IFunction1;
import com.jiefzz.ejoker.z.common.system.functional.IFunction3;
import com.jiefzz.ejoker.z.common.system.helper.StringHelper;
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;

public class EJokerBootstrap {
	
	private final static  Logger logger = LoggerFactory.getLogger(EJokerBootstrap.class);

//	protected final static String EJokerDefaultImplPackage = "com.jiefzz.eDefaultImpl";
	protected final static String EJokerDefaultImplPackage = "pro.jiefzz.ejoker.demo.completion";
	
	protected final static String BusinessPackage = "pro.jiefzz.ejoker.demo.simple.transfer";
	
	public final static String NameServAddr = "192.168.199.94:9876;192.168.199.123:9876";
	
	public final static String EJokerDomainEventConsumerGroup = "EjokerDomainEventConsumerGroup";
	public final static String EJokerDomainEventProducerGroup = "EjokerDomainEventProducerGroup";
	
	public final static String EJokerCommandConsumerGroup = "EjokerCommandConsumerGroup";
	public final static String EJokerCommandProducerGroup = "EjokerCommandProducerGroup";

	public final static String EJokerApplicationMessageConsumerGroup = "EjokerApplicationMessageConsumerGroup";
	public final static String EJokerApplicationMessageProducerGroup = "EjokerApplicationMessageProducerGroup";

	public final static String EJokerPublishableExceptionConsumerGroup = "EjokerPublishableExceptionConsumerGroup";
	public final static String EJokerPublishableExceptionProducerGroup = "EjokerPublishableExceptionProducerGroup";
	
	
	public final static long BatchDelay = 10000l;
	
	protected final IEJokerSimpleContext eJokerContext;

	protected final Scavenger scavenger;
	
	protected final SystemAsyncHelper systemAsyncHelper;
	
	protected final IFunction3<MessageQueue, List<MessageQueue>, Message, Object> mqSelector = 
			(mqs, msg, extObj) -> {
				String keys = msg.getKeys();
				int mqIndex = 0;
				if(StringHelper.isNullOrWhiteSpace(keys)) {
					// 按时间选择
					mqIndex = (int )System.currentTimeMillis()%mqs.size();
				} else {
					// 按哈希取模选择
					// 这个位置可以按需要使用一致性哈希的选择逻辑
					byte[] bytes = keys.getBytes();
					int total = 0;
					for(int i = 0; i<bytes.length; i++) {
						total += bytes[i];
					}
					mqIndex = total%mqs.size();
				}
				return mqs.get(mqIndex);
			};

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
		{
			commandResultProcessor.start();
			scavenger.addFianllyJob(commandResultProcessor::shutdown);
		}
		return commandResultProcessor;
		
	}
	
	/* ========================= */
	
	public DomainEventConsumer initDomainEventConsumer() throws Exception {
		return initDomainEventConsumer(mq -> true);
	}

	public DomainEventConsumer initDomainEventConsumer(IFunction1<Boolean, MessageQueue> queueMatcher) throws Exception {

		// 启动领域事件消费者
		DomainEventConsumer domainEventConsumer = eJokerContext.get(DomainEventConsumer.class);
		if(cTables[0].compareAndSet(false, true)) {
			DefaultMQConsumer defaultMQConsumer = new DefaultMQConsumer(EJokerDomainEventConsumerGroup);
			defaultMQConsumer.setNamesrvAddr(NameServAddr);
			defaultMQConsumer.useSubmiter(vf -> systemAsyncHelper.submit(vf::trigger));
			defaultMQConsumer.useQueueSelector(queueMatcher);
			
			domainEventConsumer.useConsumer(defaultMQConsumer).subscribe(TopicReference.DomainEventTopic).start();
			scavenger.addFianllyJob(domainEventConsumer::shutdown);
		}
		return domainEventConsumer;
		
	}
	
	public DomainEventPublisher initDomainEventPublisher() {

		// 启动领域事件发布者
		DomainEventPublisher domainEventPublisher = eJokerContext.get(DomainEventPublisher.class);
		if(cTables[1].compareAndSet(false, true)) {
			DefaultMQProducer defaultMQProducer = new DefaultMQProducer(EJokerDomainEventProducerGroup);
			defaultMQProducer.setNamesrvAddr(NameServAddr);
			defaultMQProducer.configureMQSelector(this.mqSelector);
			
			domainEventPublisher.useProducer(defaultMQProducer).start();
			scavenger.addFianllyJob(defaultMQProducer::shutdown);
		}
		return domainEventPublisher;
		
	}
	
	/* ========================= */

	public CommandConsumer initCommandConsumer() throws Exception {
		return initCommandConsumer(mq -> true);
	}
	
	public CommandConsumer initCommandConsumer(IFunction1<Boolean, MessageQueue> queueMatcher) throws Exception {

		// 启动命令消费者
		CommandConsumer commandConsumer = eJokerContext.get(CommandConsumer.class);
		if(cTables[2].compareAndSet(false, true)) {
			DefaultMQConsumer defaultMQConsumer = new DefaultMQConsumer(EJokerCommandConsumerGroup);
			defaultMQConsumer.setNamesrvAddr(NameServAddr);
			defaultMQConsumer.useSubmiter(vf -> systemAsyncHelper.submit(vf::trigger));
			defaultMQConsumer.useQueueSelector(queueMatcher);
			
//			defaultMQConsumer.configureFlowControl(true);
//			defaultMQConsumer.useFlowControlSwitch((mq, amount, loopCount) -> {
//				if(amount - 75000 > 0)
//					return true;
//				return false;
//			});
			
			commandConsumer.useConsumer(defaultMQConsumer).subscribe(TopicReference.CommandTopic).start();
			scavenger.addFianllyJob(commandConsumer::shutdown);
		}
		return commandConsumer;
		
	}
	
	public CommandService initCommandService() {
		// 启动命令服务
		CommandService commandService = eJokerContext.get(CommandService.class);
		if(cTables[3].compareAndSet(false, true)) {

			initCommandResultProcessor();
			
			DefaultMQProducer defaultMQProducer = new DefaultMQProducer(EJokerCommandProducerGroup);
			defaultMQProducer.setNamesrvAddr(NameServAddr);
			defaultMQProducer.configureMQSelector(this.mqSelector);
			
			commandService.useProducer(defaultMQProducer).start();
			scavenger.addFianllyJob(commandService::shutdown);
		}
		return commandService;
	}
	
	/* ========================= */

	public ApplicationMessageConsumer initApplicationMessageConsumer() throws Exception {
		return initApplicationMessageConsumer(mq -> true);
	}

	public ApplicationMessageConsumer initApplicationMessageConsumer(IFunction1<Boolean, MessageQueue> queueMatcher) throws Exception {

		// 启动应用信息消费者
		ApplicationMessageConsumer applicationMessageConsumer = eJokerContext.get(ApplicationMessageConsumer.class);
		if(cTables[4].compareAndSet(false, true)) {
			DefaultMQConsumer defaultMQConsumer = new DefaultMQConsumer(EJokerApplicationMessageConsumerGroup);
			defaultMQConsumer.setNamesrvAddr(NameServAddr);
			defaultMQConsumer.useSubmiter(vf -> systemAsyncHelper.submit(vf::trigger));
			defaultMQConsumer.useQueueSelector(queueMatcher);
			
			applicationMessageConsumer.useConsumer(defaultMQConsumer).subscribe(TopicReference.ApplicationMessageTopic).start();
			scavenger.addFianllyJob(applicationMessageConsumer::shutdown);
		}
		return applicationMessageConsumer;
		
	}
	
	public ApplicationMessagePublisher initApplicationMessageProducer() {
		// 启动应用信息生产者
		ApplicationMessagePublisher applicationMessageProducer = eJokerContext.get(ApplicationMessagePublisher.class);
		if(cTables[5].compareAndSet(false, true)) {
			DefaultMQProducer defaultMQProducer = new DefaultMQProducer(EJokerApplicationMessageProducerGroup);
			defaultMQProducer.setNamesrvAddr(NameServAddr);
			defaultMQProducer.configureMQSelector(this.mqSelector);
			
			applicationMessageProducer.useProducer(defaultMQProducer).start();
			scavenger.addFianllyJob(applicationMessageProducer::shutdown);
		}
		return applicationMessageProducer;
	}
	
	/* ========================= */

	public PublishableExceptionConsumer initPublishableExceptionConsumer() throws Exception {
		return initPublishableExceptionConsumer(mq -> true);
	}

	public PublishableExceptionConsumer initPublishableExceptionConsumer(IFunction1<Boolean, MessageQueue> queueMatcher) throws Exception {

		// 启动可发布异常消费者
		PublishableExceptionConsumer publishableExceptionConsumer = eJokerContext.get(PublishableExceptionConsumer.class);
		if(cTables[6].compareAndSet(false, true)) {
			DefaultMQConsumer defaultMQConsumer = new DefaultMQConsumer(EJokerPublishableExceptionConsumerGroup);
			defaultMQConsumer.setNamesrvAddr(NameServAddr);
			defaultMQConsumer.useSubmiter(vf -> systemAsyncHelper.submit(vf::trigger));
			defaultMQConsumer.useQueueSelector(queueMatcher);
			
			publishableExceptionConsumer.useConsumer(defaultMQConsumer).subscribe(TopicReference.ExceptionTopic).start();
			scavenger.addFianllyJob(publishableExceptionConsumer::shutdown);
		}
		return publishableExceptionConsumer;
		
	}
	
	public PublishableExceptionPublisher initPublishableExceptionProducer() {
		// 启动可发布异常生产者
		PublishableExceptionPublisher publishableExceptionProducer = eJokerContext.get(PublishableExceptionPublisher.class);
		if(cTables[7].compareAndSet(false, true)) {
			DefaultMQProducer defaultMQProducer = new DefaultMQProducer(EJokerPublishableExceptionProducerGroup);
			defaultMQProducer.setNamesrvAddr(NameServAddr);
			defaultMQProducer.configureMQSelector(this.mqSelector);
			
			publishableExceptionProducer.useProducer(defaultMQProducer).start();
			scavenger.addFianllyJob(publishableExceptionProducer::shutdown);
		}
		return publishableExceptionProducer;
	}
}
