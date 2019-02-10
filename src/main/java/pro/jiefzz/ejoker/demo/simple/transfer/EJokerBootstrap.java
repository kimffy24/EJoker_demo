package pro.jiefzz.ejoker.demo.simple.transfer;

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
import com.jiefzz.ejoker.z.common.task.context.SystemAsyncHelper;

public class EJokerBootstrap {

	protected final static String EJokerDefaultImplPackage = "com.jiefzz.eDefaultImpl";
	
	protected final static String BusinessPackage = "pro.jiefzz.ejoker.demo.simple.transfer";
	
//	public final static String NameServAddr = "192.168.199.94:9876;192.168.199.123:9876";
	public final static String NameServAddr = "192.168.1.2:9876";
	
	public final static String EJokerDomainEventConsumerGroup = "EjokerDomainEventConsumerGroup";
	
	public final static String EJokerCommandConsumerGroup = "EjokerCommandConsumerGroup";
	
	public final static long BatchDelay = 6000l;
	
	protected final IEJokerSimpleContext eJokerContext;

	protected final Scavenger scavenger;
	
	protected final SystemAsyncHelper systemAsyncHelper;

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
	
	public CommandResultProcessor initCommandResultProcessor() {

//		 启动命令跟踪反馈控制对象
		CommandResultProcessor commandResultProcessor = eJokerContext.get(CommandResultProcessor.class);
		{
			commandResultProcessor.start();
			scavenger.addFianllyJob(commandResultProcessor::shutdown);
		}
		return commandResultProcessor;
		
	}
	
	public DomainEventConsumer initDomainEventConsumer() throws Exception {

		// 启动领域事件消费者
		DomainEventConsumer domainEventConsumer = eJokerContext.get(DomainEventConsumer.class);
		{
			DefaultMQConsumer defaultMQConsumer = new DefaultMQConsumer(EJokerDomainEventConsumerGroup);
			defaultMQConsumer.setNamesrvAddr(NameServAddr);
			defaultMQConsumer.useSubmiter((vf) -> {
				systemAsyncHelper.submit(vf::trigger);
			});
			
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
			
			domainEventPublisher.useProducer(defaultMQProducer).start();
			scavenger.addFianllyJob(defaultMQProducer::shutdown);
		}
		return domainEventPublisher;
		
	}
	
	public CommandConsumer initCommandConsumer() throws Exception {

		// 启动命令消费者
		CommandConsumer commandConsumer = eJokerContext.get(CommandConsumer.class);
		{
			DefaultMQConsumer defaultMQConsumer = new DefaultMQConsumer(EJokerCommandConsumerGroup);
			defaultMQConsumer.setNamesrvAddr(NameServAddr);
			defaultMQConsumer.useSubmiter((vf) -> {
				systemAsyncHelper.submit(vf::trigger);
			});
			
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
			commandService.useProducer(defaultMQProducer).start();
			scavenger.addFianllyJob(commandService::shutdown);
		}
		return commandService;
	}
	
}
