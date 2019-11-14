package pro.jiefzz.ejoker_demo.transfer.debug;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.common.system.functional.IFunction;
import pro.jiefzz.ejoker.common.system.functional.IVoidFunction1;
import pro.jiefzz.ejoker.queue.command.CommandConsumer;
import pro.jiefzz.ejoker.queue.domainEvent.DomainEventConsumer;
import pro.jiefzz.ejoker.queue.skeleton.aware.EJokerQueueMessage;
import pro.jiefzz.ejoker.queue.skeleton.aware.IConsumerWrokerAware;
import pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.mqmock.ICQProvider;
import pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.mqmock.MQConsumerMemoryAdapter;

@EService
public class DebugHelperJavaQueue extends DAssemblier {
	
	private final static Logger logger = LoggerFactory.getLogger(DebugHelperJavaQueue.class);

	@Dependence
	CommandConsumer commandConsumer;
	
	@Dependence
	DomainEventConsumer eventConsumer;
	
	@Dependence
	DebugHelperEJoker debugHelperEJoker;
	
	Queue<EJokerQueueMessage> commandQueue = null;
	AtomicBoolean commandHandledStatictics = null;
	AtomicLong commandHandledAmount = null;
	AtomicLong commandHandledStartAt = null;
	Queue<Long> commandHandledLatestAll = null;
	
	long commandHandledLatestAt = 0l;

	AtomicBoolean sepOnce = null;
	CountDownLatch blocker = null;
	
	
	Queue<EJokerQueueMessage> eventQueue = null;
	AtomicBoolean eventHandledStatictics = null;
	AtomicLong eventHandledAmount = null;
	AtomicLong eventHandledStartAt = null;
	Queue<Long> eventHandledLatestAll = null;
	
	long eventHandledLatestAt = 0l;
	
	@Override
	protected boolean tryAssembly() {
		IConsumerWrokerAware deeplyCommandConsumer = commandConsumer.getDeeplyConsumer();
		IConsumerWrokerAware deeplyEventConsumer = eventConsumer.getDeeplyConsumer();
		
		if(!(deeplyCommandConsumer instanceof MQConsumerMemoryAdapter)) {
			return false;
		}
		
		connect(deeplyCommandConsumer, "queue", "commandQueue");

		connect(deeplyEventConsumer, "queue", "eventQueue");

		try {

			connect(deeplyEventConsumer, "dispatchStatictics", "eventHandledStatictics");
			connect(deeplyEventConsumer, "dispatchAmount", "eventHandledAmount");
			connect(deeplyEventConsumer, "dispatchStartAt", "eventHandledStartAt");
			connect(deeplyEventConsumer, "dispatchLatestAll", "eventHandledLatestAll");
			
			connect(deeplyCommandConsumer, "dispatchStatictics", "commandHandledStatictics");
			connect(deeplyCommandConsumer, "dispatchAmount", "commandHandledAmount");
			connect(deeplyCommandConsumer, "dispatchStartAt", "commandHandledStartAt");
			connect(deeplyCommandConsumer, "dispatchLatestAll", "commandHandledLatestAll");

			connect(deeplyCommandConsumer, "sepOnce");
			connect(deeplyCommandConsumer, "blocker");
			
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public void sepOnce() {
		isUse();
		sepOnce.set(true);
	}
	
	@Override
	protected boolean isActive() {
		if(null == commandHandledStatictics)
			return false;
		return (commandHandledStatictics.get()
				&& commandHandledLatestAt + 25000l > System.currentTimeMillis())
				|| !commandHandledStatictics.get();
	}

	public void turnOnStatictics() {
		
		if(!isUse())
			return;
		
		if(null != eventHandledStatictics) {
			eventHandledStatictics.set(true);
			eventHandledStartAt.set(0l);
			eventHandledAmount.set(0l);
			eventHandledLatestAll.clear();
		}
		
		if(null != commandHandledStatictics) {
			commandHandledStatictics.set(true);
			commandHandledStartAt.set(0l);
			commandHandledAmount.set(0l);
			commandHandledLatestAll.clear();
		}
		

		ICQProvider.mockMsgQueues.forEach((t, dsh) -> dsh.ai.set(0));
		
		blocker.countDown();
	}
	
	public void turnOffStatictics() {
		
		if(!isUse())
			return;
		
		if(null != eventHandledStatictics) {
			eventHandledStatictics.set(false);
			eventHandledStartAt.set(-1l);
			eventHandledAmount.set(0l);
			eventHandledLatestAll.clear();
		}
		if(null != commandHandledStatictics) {
			commandHandledStatictics.set(false);
			commandHandledStartAt.set(-1l);
			commandHandledAmount.set(0l);
			commandHandledLatestAll.clear();
		}
	}

	public void probe() {
		
		if(!isUse())
			return;
		
		logger.error("CommandQueue delay: {}", commandQueue.size());
		logger.error("EventQueue delay: {}", eventQueue.size());
		
		ICQProvider.mockMsgQueues.forEach((t, dsh) -> {
			logger.error("queue of topic: {}, total send: {}", t, dsh.ai.get());
		});
		
	}
	
	public void probeStatictics() {
		
		handledStatictics("command",
				commandHandledStatictics,
				commandHandledAmount,
				commandHandledStartAt,
				commandHandledLatestAll,
				() -> this.commandHandledLatestAt,
				t -> this.commandHandledLatestAt = t
				);
		
		handledStatictics("event",
				eventHandledStatictics,
				eventHandledAmount,
				eventHandledStartAt,
				eventHandledLatestAll,
				() -> this.eventHandledLatestAt,
				t -> this.eventHandledLatestAt = t
				);
		
	}
	
	private void handledStatictics(
			String messageType,
			AtomicBoolean handledStatictics,
			AtomicLong handledAmount,
			AtomicLong handledStartAt,
			Queue<Long> handledLatestAll,
			IFunction<Long> ltProvider,
			IVoidFunction1<Long> ltUpdater) {

		if(null == handledStatictics || !handledStatictics.get() || 0 >= handledStartAt.get())
			return;
		
		Long newTime = 0l;
		while(null != (newTime = handledLatestAll.poll())) {
			if(newTime - ltProvider.trigger() > 0l)
				ltUpdater.trigger(newTime);
		}
		newTime = ltProvider.trigger();
		
		try {
			BigDecimal totalProcess = new BigDecimal(""+handledAmount.get())
					.divide(
							new BigDecimal("" + (newTime - handledStartAt.get())).divide(new BigDecimal("1000")),
							RoundingMode.HALF_UP)
					;
			logger.error("message[topic={}], totalHandled: {}, handled per second: {}, first handled at: {}, latest handled at: {}, diff: {}",
					messageType, handledAmount.get(), totalProcess.toPlainString(), handledStartAt.get(), newTime, newTime - handledStartAt.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
