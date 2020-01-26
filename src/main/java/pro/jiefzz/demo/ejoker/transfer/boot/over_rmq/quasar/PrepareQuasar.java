package pro.jiefzz.demo.ejoker.transfer.boot.over_rmq.quasar;

import co.paralleluniverse.fibers.SuspendExecution;
import pro.jiefzz.demo.ejoker.transfer.boot.TransferConst;
import pro.jiefzz.demo.ejoker.transfer.boot.over_rmq.Prepare;
import pro.jiefzz.ejoker.bootstrap.EJokerQuasarBootstrap;

public class PrepareQuasar extends Prepare {

	public PrepareQuasar(String... packages) throws SuspendExecution {
		super(new EJokerQuasarBootstrap(TransferConst.EBusinessPackage, TransferConst.EJokerDefaultImplPackage));
	}

}
