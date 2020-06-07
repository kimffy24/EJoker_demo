package pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferConst;
import pro.jiefzz.demo.ejoker.transfer.boot.TransferPrepare;
import pro.jk.ejoker_support.bootstrap.EJokerBootstrap;
import pro.jk.ejoker_support.javaqueue.MQConsumerMemoryAdapter;
import pro.jk.ejoker_support.javaqueue.MQProducerMomoryAdapter;

public class Prepare {
	
	protected final EJokerBootstrap eb;
	
	public Prepare() {
		(this.eb = new EJokerBootstrap(TransferConst.EBusinessPackage, TransferConst.EJokerDefaultImplPackage))
			.setConsumerInstanceCreator((groupName, eContext) -> new MQConsumerMemoryAdapter())
			.setProducerInstanceCreator((groupName, eContext) -> new MQProducerMomoryAdapter())
			.setPreInitAll(TransferPrepare::prepare)
			.initAll();
	}

	public EJokerBootstrap getEb() {
		return eb;
	}
	
}
