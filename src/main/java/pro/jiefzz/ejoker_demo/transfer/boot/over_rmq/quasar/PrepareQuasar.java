package pro.jiefzz.ejoker_demo.transfer.boot.over_rmq.quasar;

import co.paralleluniverse.fibers.SuspendExecution;
import pro.jiefzz.ejoker.bootstrap.EJokerQuasarBootstrap;
import pro.jiefzz.ejoker_demo.transfer.boot.TransferConst;
import pro.jiefzz.ejoker_demo.transfer.boot.over_rmq.Prepare;

public class PrepareQuasar extends Prepare {

	public PrepareQuasar(String... packages) throws SuspendExecution {
		super(new EJokerQuasarBootstrap(TransferConst.EBusinessPackage, TransferConst.EJokerDefaultImplPackage));
	}

}
