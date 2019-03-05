package pro.jiefzz.ejoker.demo.simple.transfer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.eventing.IEventStore;
import com.jiefzz.ejoker.eventing.impl.InMemoryEventStore;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;

/**
 * 这是一个C端的demo<br />
 * <br />* env EJokerNodeAddr="192.168.199.123" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args="-server -Xms4g -Xmx8g -Xmn3g -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferN2"
 * <br />* 远程调试添加到exec.args中 -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7900,suspend=n
 * @author kimffy
 *
 */
public class TransferN1 {
	
	private final static  Logger logger = LoggerFactory.getLogger(TransferN1.class);

	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()));
	}
	
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {

		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
		
		IEventStore eventStore = eJokerContext.get(IEventStore.class);

		if(InMemoryEventStore.class.isAssignableFrom(eventStore.getClass())) {
			InMemoryEventStore es = (InMemoryEventStore )eventStore;
			final AtomicLong lastTotal = new AtomicLong(0);
			scheduleService.startTask("afrqgqhersxx", () -> {
			}, 1000l, 1000l);
			
		}

		eJokerFrameworkInitializer.initDomainEventPublisher();
		eJokerFrameworkInitializer.initCommandConsumer();
	}

	private final static AtomicInteger x = new AtomicInteger(0);
}
