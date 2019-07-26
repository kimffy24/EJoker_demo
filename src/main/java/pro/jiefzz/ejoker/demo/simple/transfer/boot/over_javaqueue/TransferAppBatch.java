package pro.jiefzz.ejoker.demo.simple.transfer.boot.over_javaqueue;

import pro.jiefzz.ejoker.demo.simple.transfer.boot.TransferPrepare;

public class TransferAppBatch extends pro.jiefzz.ejoker.demo.simple.transfer.boot.over_rmp.TransferAppBatch {
	
	public static void main(String[] args) throws Exception {
		start(TransferPrepare.prepare(new EJokerBootstrap()), getAccountAmount(), getDepositLoop());
	}

}
