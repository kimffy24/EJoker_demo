package pro.jiefzz.demo.ejoker.transfer.boot.over_rmq;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferConst;
import pro.jiefzz.demo.ejoker.transfer.boot.TransferPrepare;
import pro.jk.ejoker_support.bootstrap.EJokerBootstrap;
import pro.jk.ejoker_support.rocketmq.MQInstanceHelper;

public class Prepare {
	
	public final static String NameServAddr = "172.16.1.11:9876";
	
	protected final EJokerBootstrap eb;
	
	public Prepare() {
		(this.eb = new EJokerBootstrap(TransferConst.EBusinessPackage, TransferConst.EJokerDefaultImplPackage))
			.setConsumerInstanceCreator((groupName, eContext) -> MQInstanceHelper.createDefaultMQConsumer(groupName, NameServAddr, eContext))
			.setProducerInstanceCreator((groupName, eContext) -> MQInstanceHelper.createDefaultMQProducer(groupName, NameServAddr, eContext))
			.setPreInitAll(TransferPrepare::prepare)
			.initAll();
	}

	public EJokerBootstrap getEb() {
		return eb;
	}
	
}
