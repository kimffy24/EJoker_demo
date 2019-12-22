package pro.jiefzz.ejoker_demo.transfer.debug;

import java.lang.reflect.Field;
import java.util.Map;

import org.apache.rocketmq.common.message.MessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pro.jiefzz.ejoker.common.context.annotation.context.Dependence;
import pro.jiefzz.ejoker.common.context.annotation.context.EService;
import pro.jiefzz.ejoker.queue.command.CommandConsumer;
import pro.jiefzz.ejoker.queue.domainEvent.DomainEventConsumer;
import pro.jiefzz.ejoker_support.rocketmq.DefaultMQConsumer;
import pro.jiefzz.ejoker_support.rocketmq.DefaultMQConsumer.ControlStruct;

@EService
public class DebugHelperMQ extends DAssemblier {
	
	private final static Logger logger = LoggerFactory.getLogger(DebugHelperMQ.class);
	
	@Dependence
	private CommandConsumer commandConsumer;
	
	@Dependence
	private DomainEventConsumer eventConsumer;

	static final Field DefaultMQConsumer_dashboards_field;

	static {
		try {
			DefaultMQConsumer_dashboards_field = DefaultMQConsumer.class.getDeclaredField("dashboards");
		} catch (NoSuchFieldException|SecurityException e) {
			throw new RuntimeException(e);
		}
		DefaultMQConsumer_dashboards_field.setAccessible(true);
	}

	private DefaultMQConsumer getEvtConsumer() {
		Object deeplyConsumer = eventConsumer.getDeeplyConsumer();
		if(null == deeplyConsumer)
			return null;
		if(!DefaultMQConsumer.class.isAssignableFrom(deeplyConsumer.getClass()))
			return null;
		return (DefaultMQConsumer )deeplyConsumer;
	}
	
	private DefaultMQConsumer getCmdConsumer() {
		Object deeplyConsumer = commandConsumer.getDeeplyConsumer();
		if(null == deeplyConsumer)
			return null;
		if(!DefaultMQConsumer.class.isAssignableFrom(deeplyConsumer.getClass()))
			return null;
		return (DefaultMQConsumer )deeplyConsumer;
	}
	
	private Map<MessageQueue, ControlStruct> fetchEvtDashboards() {
			Object object;
			{
				try {
					object = DefaultMQConsumer_dashboards_field.get(eventConsumer.getDeeplyConsumer());
				} catch (IllegalArgumentException|IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			return (Map<MessageQueue, ControlStruct> )object;
	}
	
	private Map<MessageQueue, ControlStruct> fetchCmdDashboards() {
			Object object;
			{
				try {
					object = DefaultMQConsumer_dashboards_field.get(commandConsumer.getDeeplyConsumer());
				} catch (IllegalArgumentException|IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			return (Map<MessageQueue, ControlStruct> )object;
	}
	
	public void probe() {

		try {
			
			if(null ==  getEvtConsumer() || null == getCmdConsumer())
				return;
			
			fetchCmdDashboards().entrySet().forEach(e -> {
				ControlStruct value = e.getValue();

				// 这里不使用并行流，普通流就行了。
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

			// 这里不使用并行流，普通流就行了。
			fetchEvtDashboards().entrySet().forEach(e -> {
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void initChild(DevHelper devHelper) {
	}
}
