package pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue;

import pro.jiefzz.ejoker.EJoker;
import pro.jiefzz.ejoker.queue.skeleton.aware.IConsumerWrokerAware;
import pro.jiefzz.ejoker.queue.skeleton.aware.IProducerWrokerAware;
import pro.jiefzz.ejoker.z.context.dev2.IEJokerSimpleContext;
import pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.mqmock.MQConsumerMemoryAdapter;
import pro.jiefzz.ejoker_demo.transfer.boot.over_javaqueue.mqmock.MQProducerMomoryAdapter;

public class EJokerBootstrap extends pro.jiefzz.ejoker_demo.transfer.boot.AbstractEJokerBootstrap {

	
	public EJokerBootstrap() {
		super();
	}

	public EJokerBootstrap(EJoker eJokerInstance) {
		super(eJokerInstance);
	}
	
	@Override
	protected IConsumerWrokerAware createDefaultMQConsumer(String groupName, IEJokerSimpleContext eContext) {
		return new MQConsumerMemoryAdapter();
	}

	@Override
	protected IProducerWrokerAware createDefaultMQProducer(String groupName, IEJokerSimpleContext eContext) {
		return new MQProducerMomoryAdapter();
	}
}
