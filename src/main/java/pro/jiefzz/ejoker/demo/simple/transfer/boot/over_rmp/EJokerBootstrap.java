package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_rmp;

import com.jiefzz.ejoker.EJoker;
import com.jiefzz.ejoker.queue.aware.IConsumerWrokerAware;
import com.jiefzz.ejoker.queue.aware.IProducerWrokerAware;
import com.jiefzz.ejoker.z.common.context.dev2.IEJokerSimpleContext;
import com.jiefzz.ejoker_support.rocketmq.MQInstanceHelper;

public class EJokerBootstrap extends pro.jiefzz.ejoker.demo.simple.transfer.boot.AbstractEJokerBootstrap {

	public final static String NameServAddr = "test_rocketmq_2:9876;test_sit_1:9876";
	
	public EJokerBootstrap() {
		super();
	}

	public EJokerBootstrap(EJoker eJokerInstance) {
		super(eJokerInstance);
	}

	@Override
	protected IConsumerWrokerAware createDefaultMQConsumer(String groupName, IEJokerSimpleContext eContext) {
		return MQInstanceHelper.createDefaultMQConsumer(groupName, NameServAddr, eContext);
	}

	@Override
	protected IProducerWrokerAware createDefaultMQProducer(String groupName, IEJokerSimpleContext eContext) {
		return MQInstanceHelper.createDefaultMQProducer(groupName, NameServAddr, eContext);
	}
}
