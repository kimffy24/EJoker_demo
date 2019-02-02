package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.eventing.IEventStore;
import com.jiefzz.ejoker.eventing.impl.InMemoryEventStore;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;
import com.jiefzz.ejoker.z.common.system.wrapper.SleepWrapper;

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
		eJokerFrameworkInitializer.initCommandService();
		eJokerFrameworkInitializer.initDomainEventConsumer();
		eJokerFrameworkInitializer.initCommandConsumer();

		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
		
		IEventStore eventStore = eJokerContext.get(IEventStore.class);

		if(InMemoryEventStore.class.isAssignableFrom(eventStore.getClass())) {
			InMemoryEventStore es = (InMemoryEventStore )eventStore;
			new Thread(() -> {
		
				int x = 0;
					
					while(true) {
						SleepWrapper.sleep(TimeUnit.SECONDS, 2l);

						
						long diff = es.getMax() - es.getMin();
						if((diff/1000) <= 0) {

							logger.error("... {}", x++);
							continue;
						}

						logger.error("min timestamp: {} ms", es.getMin());
						logger.error("max timestamp: {} ms", es.getMax());
						logger.error("time use: {} ms", diff);
						logger.error("amount of EventStream: {}", es.sizeOfMStore());
						logger.error("ES queue hit: {}", es.getESAmount());
						logger.error("avg: {}", es.getESAmount()/(diff/1000));
						
					}
						
			}).start();
		}
		
		scheduleService.startTask("afrqgqhersxx", () -> {
//			commandConsumer.d1();
//			domainEventConsumer.d1();
//			systemAsyncHelper.d1();
//			DevUtils.moniter();
		}, 1000l, 1000l);

//		TimeUnit.SECONDS.sleep(20l);
//		DevUtils.ttt();
		
		LockSupport.park();
		eJokerFrameworkInitializer.discard();
	}

}
