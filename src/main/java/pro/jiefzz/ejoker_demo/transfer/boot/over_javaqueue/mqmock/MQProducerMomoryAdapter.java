package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.mqmock;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import pro.jiefzz.ejoker.queue.aware.EJokerQueueMessage;
import pro.jiefzz.ejoker.queue.aware.IProducerWrokerAware;
import pro.jiefzz.ejoker.z.system.helper.MapHelper;

public class MQProducerMomoryAdapter implements ICQProvider, IProducerWrokerAware {

	@Override
	public void start() throws Exception {
	}

	@Override
	public void shutdown() throws Exception {
		
	}

	@Override
	public void send(EJokerQueueMessage message, String routingKey, String messageId, String version) {
		Queue<EJokerQueueMessage> queue = MapHelper.getOrAddConcurrent(mockMsgQueues, message.getTopic(), ConcurrentLinkedQueue::new);
		queue.offer(message);
	}

}
