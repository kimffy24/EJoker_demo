package pro.jiefzz.ejoker.demo.simple.transfer;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jiefzz.ejoker.infrastructure.impl.InMemoryPublishedVersionStore;
import com.jiefzz.ejoker.queue.command.CommandConsumer;
import com.jiefzz.ejoker.queue.domainEvent.DomainEventConsumer;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker.z.common.io.IOHelper;
import com.jiefzz.ejoker.z.common.schedule.IScheduleService;
import com.jiefzz.ejoker.z.common.system.helper.MapHelper;
import com.jiefzz.ejoker_support.rocketmq.DefaultMQConsumer;
import com.jiefzz.ejoker_support.rocketmq.DefaultMQConsumer.ControlStruct;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.EJokerBootstrap;

/**
 * 这是一个C端和Q端一起的demo<br />
 * <br />* env EJokerNodeAddr="192.168.199.123" mvn -Dmaven.test.skip=true clean compile exec:exec -Dexec.executable="java" -Dexec.args="-server -Xms4g -Xmx8g -Xmn3g -classpath %classpath pro.jiefzz.ejoker.demo.simple.transfer.TransferNAll"
 * <br />* 远程调试添加到exec.args中 -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=7900,suspend=n
 * @author kimffy
 *
 */
public class TransferNAll {

	private final static Logger logger = LoggerFactory.getLogger(TransferNAll.class);
	
	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()));
	}
	
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {

		eJokerFrameworkInitializer.initAll();
		
		IEJokerSimpleContext eJokerContext = eJokerFrameworkInitializer.getEJokerContext();
		IScheduleService scheduleService = eJokerContext.get(IScheduleService.class);
		InMemoryPublishedVersionStore inMemoryPublishedVersionStore = eJokerContext.get(InMemoryPublishedVersionStore.class);
		CommandConsumer commandConsumer = eJokerContext.get(CommandConsumer.class);
		DomainEventConsumer eventConsumer = eJokerContext.get(DomainEventConsumer.class);
		IOHelper ioHelper = eJokerContext.get(IOHelper.class);

		DefaultMQConsumer deeplyCommandConsumer = (DefaultMQConsumer )commandConsumer.getDeeplyConsumer();
		DefaultMQConsumer deeplyEventConsumer = (DefaultMQConsumer )eventConsumer.getDeeplyConsumer();
		
		final Field declaredField = InMemoryPublishedVersionStore.class.getDeclaredField("versionDict");
		declaredField.setAccessible(true);
		final Field declaredField2 = DefaultMQConsumer.class.getDeclaredField("dashboards");
		declaredField2.setAccessible(true);
		
		Object object;
		{
			try {
				object = declaredField.get(inMemoryPublishedVersionStore);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		final Map<String, Long> versionDict = (Map<String, Long> )object;

		Object object2;
		{
			try {
				object2 = declaredField2.get(deeplyCommandConsumer);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		final Map<MessageQueue, ControlStruct> cmdDashboards = (Map<MessageQueue, ControlStruct> )object2;

		Object object3;
		{
	
			try {
				object3 = declaredField2.get(deeplyEventConsumer);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		final Map<MessageQueue, ControlStruct> evtDashboards = (Map<MessageQueue, ControlStruct> )object3;

		scheduleService.startTask("xxx", () -> {
			logger.debug("Hello world!!!");
		}, 1000l, 1000l);

		scheduleService.startTask("afrqgqhersxxzz", () -> {
			
			DevUtils.moniterQ();
			logger.error("InMemoryPublishedVersionStore.versionDict.size = {}", versionDict.size());
			Map<String, AtomicLong> dict = new ConcurrentHashMap<>();
			versionDict.entrySet().parallelStream().map(e -> {
				return e.getValue();
			}).mapToLong(l -> MapHelper.getOrAddConcurrent(dict, "" + l, AtomicLong::new).incrementAndGet()).sum();
			logger.error("version statistics: {}", dict.toString());

			logger.error("Total retry task: {}, total faild task: {}", ioHelper.cc.get(), ioHelper.dd.get());
			
			commandConsumer.D1();
			eventConsumer.D1();
			
			cmdDashboards.entrySet().forEach(e -> {
				ControlStruct value = e.getValue();

				value.aheadCompletion.keySet().stream().sorted().reduce(value.offsetConsumedLocal.get(), (a, b) -> {
					if(0l < a) {
						if(a + 1 == b) {
							;
						} else {
							logger.error("前值: {}, 后值: {}, 队列: {}", a, b, e.getKey().toString());
						}
					}
					return b;
				});
			});

			evtDashboards.entrySet().forEach(e -> {
				ControlStruct value = e.getValue();

				value.aheadCompletion.keySet().stream().sorted().reduce(value.offsetConsumedLocal.get(), (a, b) -> {
					if(0l < a) {
						if(a + 1 == b) {
							;
						} else {
							logger.error("前值: {}, 后值: {}, 队列: {}", a, b, e.getKey().toString());
						}
					}
					return b;
				});
			});
			
		}, 5000l, 5000l);
		
	}
}
