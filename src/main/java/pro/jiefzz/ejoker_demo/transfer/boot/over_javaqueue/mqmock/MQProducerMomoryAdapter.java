package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.mqmock;

import pro.jiefzz.ejoker.queue.skeleton.aware.EJokerQueueMessage;
import pro.jiefzz.ejoker.queue.skeleton.aware.IProducerWrokerAware;
import pro.jiefzz.ejoker.z.system.functional.IVoidFunction;
import pro.jiefzz.ejoker.z.system.functional.IVoidFunction1;
import pro.jiefzz.ejoker.z.system.helper.MapHelper;

public class MQProducerMomoryAdapter implements ICQProvider, IProducerWrokerAware {

	@Override
	public void start() throws Exception {
	}

	@Override
	public void shutdown() throws Exception {
		
	}

	@Override
	public void send(EJokerQueueMessage message, String routingKey, IVoidFunction successAction,
			IVoidFunction1<String> faildAction, IVoidFunction1<Exception> exceptionAction) {
		MapHelper.getOrAddConcurrent(mockMsgQueues, message.getTopic(), () -> new DSH()).offer(message);
	}

}
