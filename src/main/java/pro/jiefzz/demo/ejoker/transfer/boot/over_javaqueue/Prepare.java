package pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferConst;
import pro.jiefzz.demo.ejoker.transfer.boot.TransferPrepare;
import pro.jiefzz.ejoker.bootstrap.EJokerBootstrap;
import pro.jiefzz.ejoker_support.javaqueue.MQConsumerMemoryAdapter;
import pro.jiefzz.ejoker_support.javaqueue.MQProducerMomoryAdapter;

public class Prepare {
	
	protected final EJokerBootstrap eb;
	
	public Prepare() {
		this(new EJokerBootstrap(TransferConst.EBusinessPackage, TransferConst.EJokerDefaultImplPackage));
	}
	
	protected Prepare(EJokerBootstrap eb) {
		(this.eb = eb)
			.setConsumerInstanceCreator((groupName, eContext) -> new MQConsumerMemoryAdapter())
			.setProducerInstanceCreator((groupName, eContext) -> new MQProducerMomoryAdapter())
			.setPreInitAll(TransferPrepare::prepare)
			.initAll();
	}

	public EJokerBootstrap getEb() {
		return eb;
	}
	
}
