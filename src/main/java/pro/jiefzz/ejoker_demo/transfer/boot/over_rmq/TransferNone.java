package pro.jiefzz.ejoker_demo.transfer.boot.over_rmq;

import java.util.concurrent.locks.LockSupport;

import pro.jiefzz.ejoker.bootstrap.EJokerBootstrap;

public class TransferNone {
	
	public static void main(String[] args) throws Exception {
		start(new Prepare().getEb());
	}

	// do nothing
	public static void start(EJokerBootstrap eJokerFrameworkInitializer) throws Exception {
		LockSupport.park();

	}
}
