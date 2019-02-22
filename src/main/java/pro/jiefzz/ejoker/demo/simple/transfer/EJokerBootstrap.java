package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.List;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.queue.command.CommandConsumer;
import com.jiefzz.ejoker.queue.command.CommandResultProcessor;
import com.jiefzz.ejoker.queue.command.CommandService;
import com.jiefzz.ejoker.queue.completation.DefaultMQConsumer;
import com.jiefzz.ejoker.queue.completation.DefaultMQProducer;
import com.jiefzz.ejoker.queue.domainEvent.DomainEventConsumer;
import com.jiefzz.ejoker.queue.domainEvent.DomainEventPublisher;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.IEjokerContextDev2;
import com.jiefzz.ejoker.z.common.scavenger.Scavenger;
import com.jiefzz.ejoker.z.common.system.functional.IFunction1;
import com.jiefzz.ejoker.z.common.system.functional.IFunction3;
import com.jiefzz.ejoker.z.common.system.helper.StringHelper;
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;

public class EJokerBootstrap {

	protected final static String EJokerDefaultImplPackage = "com.jiefzz.eDefaultImpl";
	
	protected final static String BusinessPackage = "pro.jiefzz.ejoker.demo.simple.transfer";
	
	public final static String NameServAddr = "192.168.199.94:9876;192.168.199.123:9876";
	
	public final static String EJokerDomainEventConsumerGroup = "EjokerDomainEventConsumerGroup";
	
	public final static String EJokerCommandConsumerGroup = "EjokerCommandConsumerGroup";
	
	public final static long BatchDelay = 10000l;
	
	protected final IEJokerSimpleContext eJokerContext;

	protected final Scavenger scavenger;
	
	protected final SystemAsyncHelper systemAsyncHelper;
	
	protected final IFunction3<MessageQueue, List<MessageQueue>, Message, Object> mqSelector = 
			(mqs, msg, extObj) -> {
				String keys = msg.getKeys();
				int mqIndex = 0;
				if(StringHelper.isNullOrWhiteSpace(keys)) {
					mqIndex = (int )System.currentTimeMillis()%mqs.size();
				} else {
					byte[] bytes = keys.getBytes();
					int total = 0;
					for(int i = 0; i<bytes.length; i++) {
						total += bytes[i];
					}
					mqIndex = total%mqs.size();
				}
				return mqs.get(mqIndex);
			};

	public EJokerBootstrap() {
		this(EJoker.getInstance());
	}
	
	protected EJokerBootstrap(EJoker eJokerInstance) {

		System.out.println("");
		System.out.println("====================== EJokerFramework ======================");
		System.out.println("");
		
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
	
	private CommandResultProcessor initCommandResultProcessor() {

		// 启动命令跟踪反馈控制对象
		CommandResultProcessor commandResultProcessor = eJokerContext.get(CommandResultProcessor.class);
		{
			commandResultProcessor.start();
			scavenger.addFianllyJob(commandResultProcessor::shutdown);
		}
		return commandResultProcessor;
		
	}
	
	public DomainEventConsumer initDomainEventConsumer() throws Exception {
		return initDomainEventConsumer(mq -> true);
	}

	public DomainEventConsumer initDomainEventConsumer(IFunction1<Boolean, MessageQueue> queueMatcher) throws Exception {

		// 启动领域事件消费者
		DomainEventConsumer domainEventConsumer = eJokerContext.get(DomainEventConsumer.class);
		{
			DefaultMQConsumer defaultMQConsumer = new DefaultMQConsumer(EJokerDomainEventConsumerGroup);
			defaultMQConsumer.setNamesrvAddr(NameServAddr);
			defaultMQConsumer.useSubmiter(vf -> systemAsyncHelper.submit(vf::trigger));
			defaultMQConsumer.useQueueSelector(queueMatcher);
			
			domainEventConsumer.useConsumer(defaultMQConsumer).subscribe("TopicEJokerDomainEvent").start();
			scavenger.addFianllyJob(domainEventConsumer::shutdown);
		}
		return domainEventConsumer;
		
	}
	
	public DomainEventPublisher initDomainEventPublisher() {

		// 启动领域事件发布者
		DomainEventPublisher domainEventPublisher = eJokerContext.get(DomainEventPublisher.class);
		{
			DefaultMQProducer defaultMQProducer = new DefaultMQProducer("EjokerDomainEventProducerGroup");
			defaultMQProducer.setNamesrvAddr(NameServAddr);
			defaultMQProducer.configureMQSelector(this.mqSelector);
			
			domainEventPublisher.useProducer(defaultMQProducer).start();
			scavenger.addFianllyJob(defaultMQProducer::shutdown);
		}
		return domainEventPublisher;
		
	}

	public CommandConsumer initCommandConsumer() throws Exception {
		return initCommandConsumer(mq -> true);
	}
	
	public CommandConsumer initCommandConsumer(IFunction1<Boolean, MessageQueue> queueMatcher) throws Exception {

		// 启动命令消费者
		CommandConsumer commandConsumer = eJokerContext.get(CommandConsumer.class);
		{
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
			
			commandConsumer.useConsumer(defaultMQConsumer).subscribe("TopicEJokerCommand").start();
			scavenger.addFianllyJob(commandConsumer::shutdown);
		}
		return commandConsumer;
		
	}
	
	public CommandService initCommandService() {
		initCommandResultProcessor();
		// 启动命令服务
		CommandService commandService = eJokerContext.get(CommandService.class);
		{
			DefaultMQProducer defaultMQProducer = new DefaultMQProducer("EjokerCommandProducerGroup");
			defaultMQProducer.setNamesrvAddr(NameServAddr);
			defaultMQProducer.configureMQSelector(this.mqSelector);
			
			commandService.useProducer(defaultMQProducer).start();
			scavenger.addFianllyJob(commandService::shutdown);
		}
		return commandService;
	}
	
}
