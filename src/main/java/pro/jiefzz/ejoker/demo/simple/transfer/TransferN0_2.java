package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.locks.LockSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;

/**
 * mvn exec:java -Dexec.mainClass=pro.jiefzz.ejoker.demo.simple.transfer.TransferApp
 * mvn exec:exec -Dexec.executable="java" -Dexec.args="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7900,suspend=n -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferApp"
 * @author kimffy
 *
 */
public class TransferN0_2 {
	
	@SuppressWarnings("unused")
	private final static  Logger logger = LoggerFactory.getLogger(TransferN0_2.class);

	public static void main(String[] args) throws Exception {
		start(new EJokerBootstrap());
	}
	
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {

		eJokerFrameworkInitializer.initCommandService();
		eJokerFrameworkInitializer.initDomainEventConsumer(mq -> {
			return mq.getQueueId() %2 == 1;
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
