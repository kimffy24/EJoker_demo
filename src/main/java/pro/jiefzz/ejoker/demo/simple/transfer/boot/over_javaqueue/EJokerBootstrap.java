package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_javaqueue;

import com.jiefzz.ejoker.queue.aware.IConsumerWrokerAware;
import com.jiefzz.ejoker.queue.aware.IProducerWrokerAware;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.over_javaqueue.mqmock.MQConsumerMemoryAdapter;
import pro.jiefzz.ejoker.demo.simple.transfer.boot.over_javaqueue.mqmock.MQProducerMomoryAdapter;

public class EJokerBootstrap extends pro.jiefzz.ejoker.demo.simple.transfer.boot.AbstractEJokerBootstrap {

	@Override
	protected IConsumerWrokerAware createDefaultMQConsumer(String groupName, IEJokerSimpleContext eContext) {
		return new MQConsumerMemoryAdapter();
	}

	@Override
	protected IProducerWrokerAware createDefaultMQProducer(String groupName, IEJokerSimpleContext eContext) {
		return new MQProducerMomoryAdapter();
	}
}
