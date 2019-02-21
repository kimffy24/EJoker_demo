package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.eventing.IEventStore;
import com.jiefzz.ejoker.eventing.impl.InMemoryEventStore;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.context.dev2.impl.EjokerContextDev2Impl;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;
import com.jiefzz.ejoker.z.common.system.wrapper.MittenWrapper;

/**
 * mvn exec:java -Dexec.mainClass=pro.jiefzz.ejoker.demo.simple.transfer.TransferApp
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7900,suspend=n -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferApp"
 * @author kimffy
 *
 */
public class TransferApp {

	private final static Logger logger = LoggerFactory.getLogger(TransferApp.class);
	
	public static void main(String[] args) throws Exception {
		start(new EJokerBootstrap());
	}
	
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {

		// Q端启动
		eJokerFrameworkInitializer.initCommandService();
		eJokerFrameworkInitializer.initDomainEventConsumer();
		
		// C端启动
		eJokerFrameworkInitializer.initDomainEventPublisher();
		eJokerFrameworkInitializer.initCommandConsumer();
		
		// 监视代码
		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
		IEventStore eventStore = eJokerContext.get(IEventStore.class);

		final MittenWrapper mainMitten = MittenWrapper.currentThread();
		final AtomicLong lastTotal = new AtomicLong(0);
		final AtomicInteger x = new AtomicInteger(0);
		final AtomicInteger y = new AtomicInteger(0);
		final boolean memoryStoreLoad = InMemoryEventStore.class.isAssignableFrom(eventStore.getClass());
		

		scheduleService.startTask("afrqgqhersxx", () -> {
			if(y.get() - 60 > 0) {
				MittenWrapper.unpark(mainMitten);
			};
			DevUtils.moniterQ();
			if(memoryStoreLoad) {
				InMemoryEventStore es = (InMemoryEventStore )eventStore;
				double milliDiff = es.getMax() - es.getMin();
				double secondDiff = milliDiff/1000;
				if(secondDiff <= 0) {
					logger.error("... {}", x.getAndIncrement());
					return;
				}
				DevUtils.moniterC();
				long besAmount = es.getBESAmount();
				long besDelta = besAmount - lastTotal.get();
				lastTotal.set(besAmount);
				double avg = besAmount/secondDiff;
				logger.error(" time use: {} ms", milliDiff);
				logger.error(" amount of fiber: {}", com.jiefzz.equasar.EJoker.getFiberAmount());
				logger.error(" amount of business ES: {}, delta: {}", besAmount, besDelta);
				logger.error(" avg: {}", avg);
				if(besDelta - 0 == 0) {
					y.getAndIncrement();
				} else {
					y.set(0);
				}
			}
		}, 1000l, 1000l);
		
		LockSupport.park();
		((EjokerContextDev2Impl )eJokerContext).discard();
	}
}
