package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.eventing.IEventStore;
import com.jiefzz.ejoker.eventing.impl.InMemoryEventStore;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;
import com.jiefzz.equasar.EJoker;

/**
 * mvn exec:java -Dexec.mainClass=pro.jiefzz.ejoker.demo.simple.transfer.TransferApp
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7900,suspend=n -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferApp"
 * @author kimffy
 *
 */
public class TransferN1 {
	
	@SuppressWarnings("unused")
	private final static  Logger logger = LoggerFactory.getLogger(TransferN1.class);

	public static void main(String[] args) throws Exception {
		start(new EJokerBootstrap());
	}
	
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {

		eJokerFrameworkInitializer.initDomainEventPublisher();
		eJokerFrameworkInitializer.initCommandConsumer();

		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
		
		IEventStore eventStore = eJokerContext.get(IEventStore.class);

		if(InMemoryEventStore.class.isAssignableFrom(eventStore.getClass())) {
			InMemoryEventStore es = (InMemoryEventStore )eventStore;
			final AtomicLong lastTotal = new AtomicLong(0);
			scheduleService.startTask("afrqgqhersxx", () -> {
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
//				logger.error(" amount of EventStream: {}", es.sizeOfMStore());
//				logger.error(" ES queue hit: {}", es.getESQueueHit());
				logger.error(" amount of fiber: {}", com.jiefzz.equasar.EJoker.getFiberAmount());
				logger.error(" amount of business ES: {}, delta: {}", besAmount, besDelta);
				logger.error(" avg: {}", avg);
			}, 1000l, 1000l);
			
		}

		LockSupport.park();
		eJokerFrameworkInitializer.discard();
	}

	private final static AtomicInteger x = new AtomicInteger(0);
}
