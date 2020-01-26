package pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue.quasar;

import pro.jiefzz.demo.ejoker.transfer.boot.TransferConst;
import pro.jiefzz.demo.ejoker.transfer.boot.over_javaqueue.Prepare;
import pro.jiefzz.ejoker.bootstrap.EJokerQuasarBootstrap;

public class EJokerQuasarPrepare extends Prepare {

	public EJokerQuasarPrepare() {
		// 这个类是 pro.jiefzz.equasar.EJoker
		// 而不是 pro.jiefzz.ejoker.EJoker
		super(new EJokerQuasarBootstrap(TransferConst.EBusinessPackage, TransferConst.EJokerDefaultImplPackage));
	}

}
