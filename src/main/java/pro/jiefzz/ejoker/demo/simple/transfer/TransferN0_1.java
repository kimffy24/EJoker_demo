package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;

/**
 * 这是个 1/2 Q端 消费queueId为双数的队列
 */
public class TransferN0_1 {
	
	@SuppressWarnings("unused")
	private final static  Logger logger = LoggerFactory.getLogger(TransferN0_1.class);

	public static void main(String[] args) throws Exception {
		start(new EJokerBootstrap());
	}
	
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {

		eJokerFrameworkInitializer.initCommandService();
		eJokerFrameworkInitializer.initDomainEventConsumer(mq -> {
			return mq.getQueueId() %2 == 0;
		});
		
		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);

		scheduleService.startTask("afrqgqhersxx", () -> {
			DevUtils.moniterQ();
		}, 1000l, 1000l);
		
		LockSupport.park();
		eJokerFrameworkInitializer.discard();
	}

}
