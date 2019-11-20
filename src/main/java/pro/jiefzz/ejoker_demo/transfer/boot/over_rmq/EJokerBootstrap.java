//package pro.jiefzz.ejoker_demo.transfer.boot.over_rmq;
//
//import pro.jiefzz.ejoker.EJoker;
//import pro.jiefzz.ejoker.common.context.dev2.IEJokerSimpleContext;
//import pro.jiefzz.ejoker.queue.skeleton.aware.IConsumerWrokerAware;
//import pro.jiefzz.ejoker.queue.skeleton.aware.IProducerWrokerAware;
//import pro.jiefzz.ejoker_support.rocketmq.MQInstanceHelper;
//
//public class EJokerBootstrap extends pro.jiefzz.ejoker_demo.transfer.boot.AbstractEJokerBootstrap {
//
//	public final static String NameServAddr = "test_sit_1:9876";
//	
//	public EJokerBootstrap() {
//		super();
//	}
//
//	public EJokerBootstrap(EJoker eJokerInstance) {
//		super(eJokerInstance);
//	}
//
//	@Override
//	protected IConsumerWrokerAware createDefaultMQConsumer(String groupName, IEJokerSimpleContext eContext) {
//		return MQInstanceHelper.createDefaultMQConsumer(groupName, NameServAddr, eContext);
//	}
//
//	@Override
//	protected IProducerWrokerAware createDefaultMQProducer(String groupName, IEJokerSimpleContext eContext) {
//		return MQInstanceHelper.createDefaultMQProducer(groupName, NameServAddr, eContext);
//	}
//}
